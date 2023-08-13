import math

from .conversion import parse_scene_generation_data_from_dict, convert_mesh_to_trimesh, SceneGenerationData
from .domain import SceneData, SceneModelsCoords, Transform3, Transform4, Model, GrabPoint
import numpy as np
import trimesh
from scipy.spatial.transform import Rotation as R
import poisson_disc as pd


def generate_position_random(mesh: trimesh, col_manager: trimesh.collision.CollisionManager,
                             bounds: dict, org_transforms: dict):
    result_transform: np.matrix
    max_tries = 100
    counter = 1
    while True:
        rnd_x = np.random.random_sample() * bounds["width"]
        rnd_y = np.random.random_sample() * bounds["depth"]
        rnd_z = np.random.random_sample() * bounds["height"]

        random_rot = trimesh.transformations.random_rotation_matrix()

        random_pos = trimesh.transformations.translation_matrix([rnd_x, rnd_y, rnd_z])

        result_transform = trimesh.transformations.concatenate_matrices(org_transforms["translation"], random_pos,
                                                                        org_transforms["rotation"], random_rot,
                                                                        org_transforms["scale"])

        if not col_manager.in_collision_single(mesh, result_transform) or counter >= max_tries:
            break
        counter += 1

    return result_transform


def generate_positions_random(instr: SceneGenerationData, mesh: trimesh, org_transforms: dict, max_bounds: float):
    model_transforms = []
    col_manager = trimesh.collision.CollisionManager()
    for i in range(instr.scene_definition.number_of_objects):
        res_tran: np.matrix = generate_position_random(mesh, col_manager, {
            "width": instr.scene_definition.width,
            "depth": instr.scene_definition.number_of_vertical_layers * max_bounds,
            "height": instr.scene_definition.height}, org_transforms)

        col_manager.add_object(f"{i}", mesh, transform=res_tran)

        translation = trimesh.transformations.translation_from_matrix(res_tran)
        rot = trimesh.transformations.quaternion_from_matrix(res_tran)
        scale = trimesh.transformations.scale_from_matrix(res_tran)[1]

        model_transforms.append(SceneModelsCoords(position=Transform3(x=translation[0], y=translation[1],
                                                                      z=translation[2]),
                                                  rotation=Transform4(x=rot[0], y=rot[1],
                                                                      z=rot[2], w=rot[3]),
                                                  scale=Transform3(x=scale[0], y=scale[1], z=scale[2])))

        return model_transforms


def generate_positions_poisson(max_model_bounds: float, number_of_objects: int, area_bounds: dict,
                               orig_trans: dict):
    # pd.np.random.seed(0) # TODO solve seed
    p_transforms = pd.Bridson_sampling(np.array([area_bounds["width"], area_bounds["depth"], area_bounds["height"]]),
                                       radius=max_model_bounds)

    np.random.shuffle(p_transforms)
    transform_range = min(number_of_objects, len(p_transforms))

    mod_trans = []

    for i in range(transform_range):
        rand_rot = trimesh.transformations.random_rotation_matrix()
        rand_pos = trimesh.transformations.translation_matrix([p_transforms[i][0], p_transforms[i][1],
                                                               p_transforms[i][2]])

        res_tran = trimesh.transformations.concatenate_matrices(orig_trans["translation"], rand_pos,
                                                                orig_trans["rotation"], rand_rot,
                                                                orig_trans["scale"])

        pos = trimesh.transformations.translation_from_matrix(res_tran)
        rot = trimesh.transformations.quaternion_from_matrix(res_tran)
        scale = trimesh.transformations.scale_from_matrix(res_tran)[1]

        mod_trans.append(SceneModelsCoords(position=Transform3(x=pos[0], y=pos[1], z=pos[2]),
                                           rotation=Transform4(x=rot[0], y=rot[1], z=rot[2], w=rot[3]),
                                           scale=Transform3(x=scale[0], y=scale[1], z=scale[2])))

    return mod_trans


def get_rotation_matrix(rot_transform: Transform4):
    original_rotation = R.from_quat([rot_transform.x, rot_transform.y, rot_transform.z, rot_transform.w])
    quat = original_rotation.as_quat(canonical=False)
    quat_matrix = trimesh.transformations.quaternion_matrix(quat)

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
    model_pos = np.array([model_transforms.position.x, model_transforms.position.y, model_transforms.position.z])
    gp_pos = np.array([grab_point.localPosition.x, grab_point.localPosition.y, grab_point.localPosition.z])

    tran_to_new_pos = trimesh.transformations.translation_matrix(np.add(model_pos, gp_pos))
    local_rot = get_rotation_matrix(grab_point.localRotation)
    tran_to_origin = trimesh.transformations.translation_matrix(np.negative(gp_pos))
    rot = get_rotation_matrix(model_transforms.rotation)
    tran_back_from_origin = trimesh.transformations.translation_matrix(gp_pos)
    scale = trimesh.transformations.scale_matrix(1)

    res_tran = trimesh.transformations.concatenate_matrices(tran_to_new_pos, tran_to_origin, rot, tran_back_from_origin,
                                                            local_rot, scale)

    pos = trimesh.transformations.translation_from_matrix(res_tran)
    rot = trimesh.transformations.quaternion_from_matrix(res_tran)
    scale = trimesh.transformations.scale_from_matrix(res_tran)[1]

    return SceneModelsCoords(position=Transform3(x=pos[0], y=pos[1], z=pos[2]),
                             rotation=Transform4(x=rot[0], y=rot[1], z=rot[2], w=rot[3]),
                             scale=Transform3(x=scale[0], y=scale[1], z=scale[2]))


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

    original_translation = trimesh.transformations.translation_matrix(
        [model.position.x, model.position.y, model.position.z])
    org_rot_matrix = get_rotation_matrix(model.rotation)
    # TODO scale
    scale = trimesh.transformations.scale_matrix(1)

    max_bounds = np.amax(mesh.bounds) * 2

    model_transforms: list[SceneModelsCoords] = generate_model_transforms(instr, mesh, {"translation": original_translation,
                                                               "rotation": org_rot_matrix, "scale": scale}, max_bounds)

    grab_point_transforms: list[SceneModelsCoords] = []
    for model_transform in model_transforms:
        grab_point_transforms.extend(transform_grab_points_to_parent(model_transform, instr.grab_points))

    max_depth = instr.scene_definition.number_of_vertical_layers * max_bounds

    camera_pos_x = math.ceil(instr.scene_definition.width / 2)
    camera_pos_z = math.ceil(instr.scene_definition.height / 2)
    camera_pos_y = camera_pos_x + camera_pos_z + max_depth + max_bounds

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
