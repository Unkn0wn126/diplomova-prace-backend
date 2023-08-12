import json
import unittest
import src.models.domain as models
import src.models.conversion as conv


class ConvertFromDict(unittest.TestCase):
    def test_convert_transform4_from_dict(self):
        source_dict: dict = {"x": 1.0, "y": 0.5, "z": 0.1, "w": 0.2}

        result: models.Transform4 = conv.convert_transform4_from_dict(source_dict)

        assert result is not None

    def test_convert_transform3_from_dict(self):
        source_dict: dict = {"x": 1.0, "y": 0.5, "z": 0.1}

        result: models.Transform3 = conv.convert_transform3_from_dict(source_dict)

        assert result is not None

    def test_convert_transform2_from_dict(self):
        source_dict: dict = {"x": 1.0, "y": 0.5}

        result: models.Transform2 = conv.convert_transform2_from_dict(source_dict)

        assert result is not None

    def test_convert_grab_point_from_dict(self):
        source_dict: dict = {"localPosition": {"x": 1.0, "y": 0.5, "z": 0.1},
                             "localRotation": {"x": 1.0, "y": 0.5, "z": 0.1, "w": 0.2},
                             "localScale": {"x": 1.0, "y": 1.0, "z": 1.0}}

        result: models.GrabPoint = conv.convert_grab_point_from_dict(source_dict)

        assert result is not None

    def test_convert_model_mesh_face_from_dict(self):
        source_dict: dict = {"indices": [0, 2, 3]}

        result: models.ModelMeshFace = conv.convert_model_mesh_face_from_dict(source_dict)

        assert result is not None and len(result.indices) == 3

    def test_convert_model_mesh_vertex_from_dict(self):
        source_dict: dict = {"position": {"x": 0.5, "y": 0.0, "z": 0.5}, "normal": {"x": 0.0, "y": -1.0, "z": 0.0},
                             "texcoords": {"x": 0.375, "y": 0.75}, "tangent": {"x": 1.0, "y": 0.0, "z": 0.0}}

        result: models.ModelMeshVertex = conv.convert_model_mesh_vertex_from_dict(source_dict)

        assert result is not None

    def test_convert_model_mesh_from_dict(self):
        source_dict: dict
        path_to_file = './suzanne_mesh.json'
        with open(path_to_file, 'r') as f:
            source_dict = json.load(f)

        result: models.ModelMesh = conv.convert_model_mesh_from_dict(source_dict)

        assert result is not None

    def test_convert_model_from_dict(self):
        source_dict: dict
        path_to_file = './suzanne.json'
        with open(path_to_file, 'r') as f:
            source_dict = json.load(f)

        result: models.Model = conv.convert_model_from_dict(source_dict)

        assert result is not None


if __name__ == '__main__':
    unittest.main()
