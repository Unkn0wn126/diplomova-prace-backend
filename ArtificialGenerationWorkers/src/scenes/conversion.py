import src.models.conversion as m_c
from .domain import SceneModelsCoords, SceneData, SceneDefinition, SceneGenerationData
import trimesh


def parse_scene_definition_data_from_dict(source: dict) -> SceneDefinition:
    return SceneDefinition(scene_name_prefix=source["sceneNamePrefix"], width=source["width"], height=source["height"],
                           number_of_vertical_layers=source["numberOfVerticalLayers"],
                           number_of_objects=source["numberOfObjects"], scene_number=source["sceneNumber"],
                           use_simulation=source["useSimulation"], generation_algorithm=source["generationAlgorithm"],
                           aspect_ratio=source["aspectRatio"])


def parse_scene_generation_data_from_dict(source: dict) -> SceneGenerationData:
    model_dict = source.get('model')
    scene_definition_dict = source.get('sceneDefinition')

    model: m_c.Model = m_c.convert_model_from_dict(model_dict)
    grab_points: list[m_c.GrabPoint] = m_c.convert_grab_point_list_from_dict(source)
    scene_definition: SceneDefinition = parse_scene_definition_data_from_dict(scene_definition_dict)

    return SceneGenerationData(scene_definition=scene_definition, model=model, grab_points=grab_points)


def convert_mesh_to_vertex_list(source: m_c.ModelMesh):
    vertices = []

    for vertex in source.vertices:
        vertices.append([vertex.position.x, vertex.position.y, vertex.position.z])

    return vertices


def convert_mesh_to_trimesh(source: m_c.ModelMesh):
    vertices = convert_mesh_to_vertex_list(source)

    faces = []

    for face in source.faces:
        faces.append(face.indices)

    return trimesh.Trimesh(vertices=vertices, faces=faces)
