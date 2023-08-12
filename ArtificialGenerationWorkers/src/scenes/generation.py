from .conversion import parse_scene_generation_data_from_dict, SceneGenerationData
from .domain import SceneData, SceneModelsCoords, Transform3, Transform4, Model, GrabPoint


def generate_scene(source: dict) -> SceneData:
    instructions: SceneGenerationData = parse_scene_generation_data_from_dict(source)

    scene_name: str = f"{instructions.scene_definition.scene_name_prefix}{instructions.scene_definition.scene_number}"

    return SceneData(name=scene_name, height=instructions.scene_definition.height,
                     width=instructions.scene_definition.width, cameraPosition=Transform3(x=0, y=0, z=0),
                     modelTransforms=[SceneModelsCoords(position=Transform3(x=0, y=0, z=0),
                                                        rotation=Transform4(x=0, y=0, z=0, w=1.0))],
                     grabPointTransforms=[SceneModelsCoords(position=Transform3(x=0, y=0, z=0),
                                                            rotation=Transform4(x=0, y=0, z=0, w=1.0))])
