import assimp_py
import src.model_parsing.domain.models_data as models

# TODO change naming scheme to something more appropriate for Python

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

    return models.Model(mesh=meshes[0], position=models.Transform3(x=0, y=0, z=0))


def create_model_mesh(mesh) -> models.ModelMesh:
    vertices: list[models.ModelMeshVertex] = []
    faces: list[models.ModelMeshFace] = []

    positions = [] or mesh.vertices
    normals = [] or mesh.normals
    texcoords = [] or mesh.texcoords
    tangents = [] or mesh.tangents
    indices = [] or mesh.indices

    for i in range(len(positions)):
        curr_pos = positions[i]
        curr_normal = () or normals[i]
        curr_texcoords = []
        for texcoord in texcoords:
            curr_texcoords.append(() or texcoord[i])
        curr_tangent = () or tangents[i]

        vertices.append(models.ModelMeshVertex(position=create_transform3(curr_pos),
                                               normal=create_transform3(curr_normal), texcoords=create_transform2(curr_texcoords[0] or ()),
                                               tangent=create_transform3(curr_tangent)))

    for index in indices:
        curr_indices = () or index

        faces.append(models.ModelMeshFace(indices=curr_indices))

    return models.ModelMesh(vertices=vertices, faces=faces)


def create_transform3(input_tuple):
    x = input_tuple[0] or 0
    y = input_tuple[1] or 0
    z = input_tuple[2] or 0

    return models.Transform3(x=x, y=y, z=z)


def create_transform2(input_tuple):
    x = input_tuple[0] or 0
    y = input_tuple[1] or 0

    return models.Transform2(x=x, y=y)
