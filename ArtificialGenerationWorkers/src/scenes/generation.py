import math

from .conversion import parse_scene_generation_data_from_dict, convert_mesh_to_trimesh, SceneGenerationData
from .domain import SceneData, SceneModelsCoords, Transform3, Transform4, Model, GrabPoint
import numpy as np
import trimesh
import poisson_disc as pd
from pyrr import Vector3, Matrix44


def convert_matrix_to_scene_models_coords(res_tran: Matrix44):
    scale_rot_pos = res_tran.decompose()

    pos = scale_rot_pos[2]
    rot = scale_rot_pos[1]
    scale = scale_rot_pos[0]

    return SceneModelsCoords(position=Transform3(x=pos[0], y=pos[1], z=pos[2]),
                             rotation=Transform4(x=rot[0], y=rot[1], z=rot[2], w=rot[3]),
                             scale=Transform3(x=scale[0], y=scale[1], z=scale[2]))


def generate_position_random(rng, mesh: trimesh, col_manager: trimesh.collision.CollisionManager,
                             bounds: dict, org_transforms: dict) -> Matrix44:
    result_transform: Matrix44
    trimesh_transform: np.ndarray
    was_success: bool
    max_tries = 100
    counter = 1
    while True:
        rnd_x = rng.uniform(low=0, high=bounds["width"], size=1)[0]
        rnd_y = rng.uniform(low=0, high=bounds["depth"], size=1)[0]
        rnd_z = rng.uniform(low=0, high=bounds["height"], size=1)[0]

        random_rot = np.transpose(trimesh.transformations.random_rotation_matrix())
        random_pos = Matrix44.from_translation(Vector3([rnd_x, rnd_y, rnd_z]))

        tran_matrix = org_transforms["translation"] * random_pos
        rot_matrix = org_transforms["rotation"] * random_rot
        scale_matrix = org_transforms["scale"]

        result_transform = tran_matrix * rot_matrix * scale_matrix

        if not col_manager.in_collision_single(mesh, np.transpose(result_transform)):
            was_success = True
            break
        elif counter >= max_tries:
            was_success = False
            break
        counter += 1

    return result_transform if was_success else None


def generate_positions_random(instr: SceneGenerationData, mesh: trimesh, org_transforms: dict, max_bounds: float):
    model_transforms = []
    col_manager = trimesh.collision.CollisionManager()
    rng = np.random.default_rng()
    for i in range(instr.scene_definition.number_of_objects):
        res_tran: Matrix44 = generate_position_random(rng, mesh, col_manager, {
            "width": instr.scene_definition.width,
            "depth": instr.scene_definition.number_of_vertical_layers * max_bounds,
            "height": instr.scene_definition.height}, org_transforms)

        if res_tran is not None:
            col_manager.add_object(f"{i}", mesh, transform=np.transpose(res_tran))
            model_transforms.append(convert_matrix_to_scene_models_coords(res_tran))

    return model_transforms


def generate_positions_poisson(max_model_bounds: float, number_of_objects: int, area_bounds: dict,
                               orig_trans: dict):
    # pd.np.random.seed(0) # TODO solve seed
    p_transforms = pd.Bridson_sampling(np.array([area_bounds["width"], area_bounds["depth"], area_bounds["height"]]),
                                       radius=max_model_bounds, k=1000)

    np.random.shuffle(p_transforms)
    transform_range = min(number_of_objects, len(p_transforms))

    mod_trans = []

    for i in range(transform_range):
        rand_rot = np.transpose(trimesh.transformations.random_rotation_matrix())
        rot_matrix = orig_trans["rotation"] * rand_rot

        rand_pos = Matrix44.from_translation(Vector3([p_transforms[i][0], p_transforms[i][1], p_transforms[i][2]]))
        tran_matrix = orig_trans["translation"] * rand_pos

        scale_matrix = orig_trans["scale"]

        res_tran = tran_matrix * rot_matrix * scale_matrix

        mod_trans.append(convert_matrix_to_scene_models_coords(res_tran))

    return mod_trans


def get_rotation_matrix(rot_transform: Transform4):
    quaternion = np.array([rot_transform.x, rot_transform.y, rot_transform.z, rot_transform.w])
    quat_matrix = Matrix44.from_quaternion(quaternion)

    return quat_matrix


def generate_model_transforms(instr: SceneGenerationData, mesh: trimesh, org_transforms: dict, max_bounds: float):
    if instr.scene_definition.generation_algorithm == 0:
        return generate_positions_random(instr, mesh, org_transforms, max_bounds)

    elif instr.scene_definition.generation_algorithm == 1:
        return generate_positions_poisson(max_bounds, instr.scene_definition.number_of_objects, {
                "width": instr.scene_definition.width,
                "depth": instr.scene_definition.number_of_vertical_layers,
                "height": instr.scene_definition.height}, org_transforms)

    raise NotImplementedError("Invalid algorithm value")


def transform_grab_point_to_parent(model_transforms: SceneModelsCoords, grab_point: GrabPoint) -> SceneModelsCoords:
    model_pos = Vector3([model_transforms.position.x, model_transforms.position.y, model_transforms.position.z])
    gp_pos = Vector3([grab_point.localPosition.x, grab_point.localPosition.y, grab_point.localPosition.z])
    gp_scale = Vector3([grab_point.localScale.x, grab_point.localScale.y, grab_point.localScale.z])

    tran_to_new_pos = Matrix44.from_translation(model_pos + gp_pos)
    local_rot = get_rotation_matrix(grab_point.localRotation)
    tran_to_origin = Matrix44.from_translation(gp_pos.inverse)
    rot = get_rotation_matrix(model_transforms.rotation)
    tran_back_from_origin = Matrix44.from_translation(gp_pos)
    scale = Matrix44.from_scale(gp_scale)

    res_tran = tran_to_new_pos * tran_to_origin * rot * tran_back_from_origin * local_rot * scale

    return convert_matrix_to_scene_models_coords(res_tran)


def transform_grab_points_to_parent(model_transforms: SceneModelsCoords,
                                    grab_points: list[GrabPoint]) -> list[SceneModelsCoords]:
    grab_point_coords: list[SceneModelsCoords] = []

    for grab_point in grab_points:
        grab_point_coords.append(transform_grab_point_to_parent(model_transforms, grab_point))

    return grab_point_coords


def generate_scene_transforms(instr: SceneGenerationData) -> dict:
    mesh = convert_mesh_to_trimesh(instr.model.mesh)
    result: dict

    model: Model = instr.model

    original_translation = Matrix44.from_translation(Vector3([model.position.x, model.position.y, model.position.z]))
    org_rot_matrix = get_rotation_matrix(model.rotation)
    scale = Matrix44.from_scale([model.scale.x, model.scale.y, model.scale.z])

    bounds_diagonal = np.linalg.norm(mesh.bounds[0])
    min_radius = bounds_diagonal + (bounds_diagonal / 2)

    model_transforms: list[SceneModelsCoords] = generate_model_transforms(instr, mesh,
                                                                          {"translation": original_translation,
                                                                           "rotation": org_rot_matrix,
                                                                           "scale": scale}, min_radius)

    grab_point_transforms: list[SceneModelsCoords] = []
    for model_transform in model_transforms:
        grab_point_transforms.extend(transform_grab_points_to_parent(model_transform, instr.grab_points))

    camera_pos_x = math.ceil(instr.scene_definition.width / 2)
    camera_pos_z = math.ceil(instr.scene_definition.height / 2)
    camera_pos_y = instr.scene_definition.height + instr.scene_definition.number_of_vertical_layers

    result = {"modelTransforms": model_transforms,
              "grabPointsTransforms": grab_point_transforms,
              "cameraPosition": Transform3(x=camera_pos_x, y=camera_pos_y, z=camera_pos_z)}

    return result


def generate_scene(source: dict) -> SceneData:
    instructions: SceneGenerationData = parse_scene_generation_data_from_dict(source)

    scene_name: str = f"{instructions.scene_definition.scene_name_prefix}{instructions.scene_definition.scene_number}"

    transforms: dict = generate_scene_transforms(instructions)

    return SceneData(name=scene_name, height=instructions.scene_definition.height,
                     width=instructions.scene_definition.width, cameraPosition=transforms["cameraPosition"],
                     modelTransforms=transforms["modelTransforms"],
                     grabPointTransforms=transforms["grabPointsTransforms"])
