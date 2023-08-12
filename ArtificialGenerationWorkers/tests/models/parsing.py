import unittest
import src.models.domain as models
import src.models.parsing as model_parsing
import dataclasses as dc


class ParseModelFromFile(unittest.TestCase):
    def test_parse_model_from_dae_file(self):
        path_to_file = './suzanne.dae'
        model: models.Model = model_parsing.import_model_from_file(path_to_file)

        model_asdict = dc.asdict(model)

        assert model_asdict["mesh"] is not None

    def test_parse_model_from_fbx_file(self):
        path_to_file = './suzanne.fbx'
        model: models.Model = model_parsing.import_model_from_file(path_to_file)

        model_asdict = dc.asdict(model)

        assert model_asdict["mesh"] is not None

    def test_parse_model_from_glb_file(self):
        path_to_file = './suzanne.glb'
        model: models.Model = model_parsing.import_model_from_file(path_to_file)

        model_asdict = dc.asdict(model)

        assert model_asdict["mesh"] is not None

    def test_parse_model_from_gltf_file(self):
        path_to_file = './suzanne.gltf'
        model: models.Model = model_parsing.import_model_from_file(path_to_file)

        model_asdict = dc.asdict(model)

        assert model_asdict["mesh"] is not None

    def test_parse_model_from_obj_file(self):
        path_to_file = './suzanne.obj'
        model: models.Model = model_parsing.import_model_from_file(path_to_file)

        model_asdict = dc.asdict(model)

        assert model_asdict["mesh"] is not None

    def test_parse_model_from_ply_file(self):
        path_to_file = './suzanne.ply'
        model: models.Model = model_parsing.import_model_from_file(path_to_file)

        model_asdict = dc.asdict(model)

        assert model_asdict["mesh"] is not None

    def test_parse_model_from_stl_file(self):
        path_to_file = './suzanne.stl'
        model: models.Model = model_parsing.import_model_from_file(path_to_file)

        model_asdict = dc.asdict(model)

        assert model_asdict["mesh"] is not None

    def test_parse_model_from_x3d_file(self):
        path_to_file = './suzanne.x3d'
        model: models.Model = model_parsing.import_model_from_file(path_to_file)

        model_asdict = dc.asdict(model)

        assert model_asdict["mesh"] is not None


if __name__ == '__main__':
    unittest.main()
