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


def generate_position_poisson(max_model_bounds: float, number_of_objects: int,
                              area_bounds: dict, org_transforms: dict):
    # pd.np.random.seed(0) # TODO solve seed
    p_transforms = pd.Bridson_sampling(np.array([area_bounds["width"], area_bounds["depth"], area_bounds["height"]]),
                                       radius=max_model_bounds)

    model_transforms = []

    np.random.shuffle(p_transforms)

    transform_range = min(number_of_objects, len(p_transforms))

    for i in range(transform_range):

        random_rot = trimesh.transformations.random_rotation_matrix()

        random_pos = trimesh.transformations.translation_matrix([p_transforms[i][0], p_transforms[i][1],
                                                                 p_transforms[i][2]])

        result_transform = trimesh.transformations.concatenate_matrices(org_transforms["translation"], random_pos,
                                                                        org_transforms["rotation"], random_rot,
                                                                        org_transforms["scale"])

        translation = trimesh.transformations.translation_from_matrix(result_transform)
        rot = trimesh.transformations.quaternion_from_matrix(result_transform)

        model_transforms.append(SceneModelsCoords(position=Transform3(x=translation[0], y=translation[1],
                                                                      z=translation[2]),
                                                  rotation=Transform4(x=rot[0], y=rot[1],
                                                                      z=rot[2], w=rot[3])))

    return model_transforms


def get_rotation_matrix(model_rotation: Transform4):
    original_rotation = R.from_quat([model_rotation.x, model_rotation.y, model_rotation.z, model_rotation.w])

    quat = original_rotation.as_quat(canonical=False)

    quat_matrix = trimesh.transformations.quaternion_matrix(quat)

    return quat_matrix


def generate_random_model_transforms(instr: SceneGenerationData, mesh: trimesh, org_transforms: dict,
                                     max_bounds: float):
    model_transforms = []
    if instr.scene_definition.generation_algorithm == 0:
        col_manager = trimesh.collision.CollisionManager()
        for i in range(instr.scene_definition.number_of_objects):
            result_transform: np.matrix = generate_position_random(mesh, col_manager, {
                "width": instr.scene_definition.width,
                "depth": instr.scene_definition.number_of_vertical_layers * max_bounds,
                "height": instr.scene_definition.height}, org_transforms)

            col_manager.add_object(f"{i}", mesh, transform=result_transform)

            translation = trimesh.transformations.translation_from_matrix(result_transform)
            rot = trimesh.transformations.quaternion_from_matrix(result_transform)

            model_transforms.append(SceneModelsCoords(position=Transform3(x=translation[0], y=translation[1],
                                                                          z=translation[2]),
                                                      rotation=Transform4(x=rot[0], y=rot[1],
                                                                          z=rot[2], w=rot[3])))

        return model_transforms

    elif instr.scene_definition.generation_algorithm == 1:
        model_transforms = generate_position_poisson(max_bounds, instr.scene_definition.number_of_objects, {
                "width": instr.scene_definition.width,
                "depth": instr.scene_definition.number_of_vertical_layers,
                "height": instr.scene_definition.height}, org_transforms)
    else:
        raise NotImplementedError("Invalid algorithm value")

    return model_transforms


def generate_random_transforms(instr: SceneGenerationData) -> dict:
    mesh = convert_mesh_to_trimesh(instr.model.mesh)
    result: dict

    model: Model = instr.model

    original_translation = trimesh.transformations.translation_matrix(
        [model.position.x, model.position.y, model.position.z])

    org_rot_matrix = get_rotation_matrix(model.rotation)

    # TODO scale
    scale = trimesh.transformations.scale_matrix(1)

    max_bounds = np.amax(mesh.bounds) * 2

    model_transforms = generate_random_model_transforms(instr, mesh, {"translation": original_translation,
                                                               "rotation": org_rot_matrix, "scale": scale}, max_bounds)

    max_depth = instr.scene_definition.number_of_vertical_layers * max_bounds

    camera_pos_x = math.ceil(instr.scene_definition.width / 2)
    camera_pos_z = math.ceil(instr.scene_definition.height / 2)
    camera_pos_y = camera_pos_x + camera_pos_z + max_depth + max_bounds

    result = {"modelTransforms": model_transforms,
              "grabPointsTransforms": [],
              "cameraPosition": Transform3(x=camera_pos_x, y=camera_pos_y, z=camera_pos_z)}

    return result


def generate_scene(source: dict) -> SceneData:
    instructions: SceneGenerationData = parse_scene_generation_data_from_dict(source)

    scene_name: str = f"{instructions.scene_definition.scene_name_prefix}{instructions.scene_definition.scene_number}"

    transforms: dict = generate_random_transforms(instructions)

    return SceneData(name=scene_name, height=instructions.scene_definition.height,
                     width=instructions.scene_definition.width, cameraPosition=transforms["cameraPosition"],
                     modelTransforms=transforms["modelTransforms"],
                     grabPointTransforms=transforms["grabPointsTransforms"])
