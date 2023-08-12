from dataclasses import dataclass
from src.models.domain import Transform3, Transform4, Model, GrabPoint


@dataclass
class SceneModelsCoords:
    position: Transform3
    rotation: Transform4


@dataclass
class SceneData:
    name: str
    width: int
    height: int
    cameraPosition: Transform3
    modelTransforms: list[SceneModelsCoords]
    grabPointTransforms: list[SceneModelsCoords]


@dataclass
class SceneDefinition:
    scene_name_prefix: str
    width: int
    height: int
    number_of_vertical_layers: int
    number_of_objects: int
    scene_number: int
    use_simulation: bool
    generation_algorithm: int
    aspect_ratio: int


@dataclass
class SceneGenerationData:
    scene_definition: SceneDefinition
    model: Model
    grab_points: list[GrabPoint]
