import math

from .conversion import parse_scene_generation_data_from_dict, convert_mesh_to_trimesh, SceneGenerationData
from .domain import SceneData, SceneModelsCoords, Transform3, Transform4, Model, GrabPoint
import numpy as np
import trimesh
from scipy.spatial.transform import Rotation as R
from scipy.stats.qmc import PoissonDisk


def generate_random_position(mesh: trimesh, col_manager: trimesh.collision.CollisionManager,
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


def get_rotation_matrix(model_rotation: Transform4):
    original_rotation = R.from_quat([model_rotation.x, model_rotation.y, model_rotation.z, model_rotation.w])

    org_rot_rotvec = original_rotation.as_rotvec(degrees=True)

    angle = np.linalg.norm(org_rot_rotvec)

    axis = org_rot_rotvec

    rot_matrix = trimesh.transformations.rotation_matrix(angle, axis)

    return rot_matrix


def generate_random_positions(instr: SceneGenerationData) -> dict:
    mesh = convert_mesh_to_trimesh(instr.model.mesh)
    result: dict

    model: Model = instr.model

    original_translation = trimesh.transformations.translation_matrix(
        [model.position.x, model.position.y, model.position.z])

    org_rot_matrix = get_rotation_matrix(model.rotation)

    # TODO scale
    scale = trimesh.transformations.scale_matrix(1)

    max_bounds = np.amax(mesh.bounds) * 2

    col_manager = trimesh.collision.CollisionManager()

    model_matrices = []

    max_depth = instr.scene_definition.number_of_vertical_layers * max_bounds

    for i in range(instr.scene_definition.number_of_objects):
        result_transform: np.matrix = generate_random_position(mesh, col_manager, {
            "width": instr.scene_definition.width,
            "depth": instr.scene_definition.number_of_vertical_layers * max_bounds,
            "height": instr.scene_definition.height}, {"translation": original_translation, "rotation": org_rot_matrix,
                                                      "scale": scale})

        col_manager.add_object(f"{i}", mesh, transform=result_transform)
        model_matrices.append(result_transform)

    model_transforms = []

    for matrix in model_matrices:
        translation = trimesh.transformations.translation_from_matrix(matrix)
        rot = trimesh.transformations.rotation_from_matrix(matrix)
        rotation = trimesh.transformations.rotation_from_matrix(matrix)[2]
        rot_2 = R.from_rotvec(rot[1])
        rot_quat = rot_2.as_quat()
        scale = trimesh.transformations.scale_from_matrix(matrix)

        model_transforms.append(SceneModelsCoords(position=Transform3(x=translation[0], y=translation[1],
                                                                      z=translation[2]),
                                                  rotation=Transform4(x=rot_quat[0], y=rot_quat[1],
                                                                      z=rot_quat[2], w=rot_quat[3])))

    camera_pos_x = math.ceil(instr.scene_definition.width / 2)
    camera_pos_z = math.ceil(instr.scene_definition.height / 2)
    camera_pos_y = camera_pos_x + camera_pos_z + max_depth + max_bounds

    print(max_depth)
    print(max_bounds)
    print(camera_pos_y)

    result = {"modelTransforms": model_transforms,
              "grabPointsTransforms": [],
              "cameraPosition": Transform3(x=camera_pos_x, y=camera_pos_y, z=camera_pos_z)}

    return result


def generate_scene(source: dict) -> SceneData:
    instructions: SceneGenerationData = parse_scene_generation_data_from_dict(source)

    scene_name: str = f"{instructions.scene_definition.scene_name_prefix}{instructions.scene_definition.scene_number}"

    transforms: dict = generate_random_positions(instructions)

    return SceneData(name=scene_name, height=instructions.scene_definition.height,
                     width=instructions.scene_definition.width, cameraPosition=transforms["cameraPosition"],
                     modelTransforms=transforms["modelTransforms"],
                     grabPointTransforms=transforms["grabPointsTransforms"])
