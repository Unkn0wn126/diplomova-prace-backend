from dataclasses import dataclass
from src.model_parsing.domain.models_data import Transform3, Transform4


@dataclass
class SceneModelsCoords:
    position: Transform3
    rotation: Transform4


@dataclass
class SceneData:
    name: str
    width: int
    height: int
    camera_position: Transform3
    model_transforms: list[SceneModelsCoords]
    grabpoint_transforms: list[SceneModelsCoords]


@dataclass
class SceneDefinition:
    scene_name_prefix: str
    width: int
    height: int
    number_of_vertical_layers: int
    number_of_objects: int
    number_of_scenes: int
    use_simulation: bool
    generation_algorithm: int
    aspect_ratio: int
