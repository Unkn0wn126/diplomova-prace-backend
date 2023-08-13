from .domain import Model, ModelMesh, ModelMeshVertex, ModelMeshFace, Transform2, Transform3, Transform4, GrabPoint


def convert_transform4_from_dict(source: dict) -> Transform4:
    return Transform4(x=source["x"], y=source["y"], z=source["z"], w=source["w"])


def convert_transform3_from_dict(source: dict) -> Transform3:
    return Transform3(x=source["x"], y=source["y"], z=source["z"])


def convert_transform2_from_dict(source: dict) -> Transform2:
    return Transform2(x=source["x"], y=source["y"])


def convert_grab_point_from_dict(source: dict) -> GrabPoint:
    return GrabPoint(localPosition=convert_transform3_from_dict(source["localPosition"]),
                     localRotation=convert_transform4_from_dict(source["localRotation"]),
                     localScale=convert_transform3_from_dict(source["localScale"]))


def convert_grab_point_list_from_dict(source: dict) -> list[GrabPoint]:
    grab_points: list[GrabPoint] = []

    for grab_point in source["grabPoints"]:
        grab_points.append(convert_grab_point_from_dict(grab_point))

    return grab_points


def convert_model_mesh_face_from_dict(source: dict) -> ModelMeshFace:
    indices: list[int] = []

    for index in source["indices"]:
        indices.append(index)

    return ModelMeshFace(indices=indices)


def convert_model_mesh_vertex_from_dict(source: dict) -> ModelMeshVertex:
    return ModelMeshVertex(position=convert_transform3_from_dict(source["position"]),
                           normal=convert_transform3_from_dict(source["normal"]),
                           texcoords=convert_transform2_from_dict(source["texcoords"]),
                           tangent=convert_transform3_from_dict(source["tangent"]))


def convert_model_mesh_from_dict(source: dict) -> ModelMesh:
    faces: list[ModelMeshFace] = []
    vertices: list[ModelMeshVertex] = []

    for face in source["faces"]:
        faces.append(convert_model_mesh_face_from_dict(face))

    for vertex in source["vertices"]:
        vertices.append(convert_model_mesh_vertex_from_dict(vertex))

    return ModelMesh(vertices=vertices, faces=faces)


def convert_model_from_dict(source: dict) -> Model:
    return Model(mesh=convert_model_mesh_from_dict(source["mesh"]),
                 position=convert_transform3_from_dict(source["position"] or Transform3(x=0, y=0, z=0)),
                 rotation=convert_transform4_from_dict(source["rotation"] or Transform4(x=0, y=0, z=0, w=1)),
                 scale=convert_transform3_from_dict(source["scale"] or Transform3(x=0, y=0, z=0)))
