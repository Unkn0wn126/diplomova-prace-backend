import assimp_py
import src.models.domain as models

process_flags = (
        assimp_py.Process_Triangulate | assimp_py.Process_CalcTangentSpace
)


def import_model_from_file(file_name: str) -> models.Model:
    """

    :rtype: models.Model
    """
    scene = assimp_py.ImportFile(file_name, process_flags)
    meshes: list[models.ModelMesh] = []

    for m in scene.meshes:
        model_mesh = create_model_mesh(m)

        meshes.append(model_mesh)

    return models.Model(mesh=meshes[0], position=models.Transform3(x=0, y=0, z=0),
                        rotation=models.Transform4(x=0, y=0, z=0, w=1),
                        scale=models.Transform3(x=1, y=1, z=1))


def create_model_mesh(mesh) -> models.ModelMesh:
    vertices: list[models.ModelMeshVertex] = []
    faces: list[models.ModelMeshFace] = []

    positions = mesh.vertices if mesh.vertices is not None else []
    normals = mesh.normals if mesh.normals is not None else []
    texcoords = mesh.texcoords if mesh.texcoords is not None else []
    tangents = mesh.tangents if mesh.tangents is not None else []
    indices = mesh.indices if mesh.indices is not None else []

    for i in range(len(positions)):
        curr_pos = positions[i]
        curr_normal = normals[i] if len(normals) > i else ()
        curr_texcoords = []
        for texcoord in texcoords:
            curr_texcoords.append(texcoord[i] if len(texcoord) > i else ())
        curr_tangent = tangents[i] if len(tangents) > i else ()

        vertices.append(models.ModelMeshVertex(position=create_transform3(curr_pos),
                                               normal=create_transform3(curr_normal),
                                               texcoords=create_transform2(curr_texcoords[0] if
                                                                           len(curr_texcoords) > 0 else ()),
                                               tangent=create_transform3(curr_tangent)))

    for index in indices:
        curr_indices = index or ()

        faces.append(models.ModelMeshFace(indices=curr_indices))

    return models.ModelMesh(vertices=vertices, faces=faces)


def create_transform3(input_tuple):
    x = input_tuple[0] if len(input_tuple) > 0 else 0
    y = input_tuple[1] if len(input_tuple) > 1 else 0
    z = input_tuple[2] if len(input_tuple) > 2 else 0

    return models.Transform3(x=x, y=y, z=z)


def create_transform2(input_tuple):
    x = input_tuple[0] if len(input_tuple) > 0 else 0
    y = input_tuple[1] if len(input_tuple) > 1 else 0

    return models.Transform2(x=x, y=y)
