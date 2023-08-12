import json
import unittest
import src.scenes.conversion as conv


class ConvertFromDict(unittest.TestCase):
    def test_convert_scene_definition_from_dict(self):
        result: conv.SceneDefinition
        source_dict: dict
        path_to_file = './scene_definition.json'
        with open(path_to_file, 'r', encoding='UTF8') as f:
            source_dict = json.load(f)

        result = conv.parse_scene_definition_data_from_dict(source_dict)

        assert result is not None

    def test_convert_scene_generation_data_from_dict(self):
        result: conv.SceneGenerationData
        source_dict: dict
        path_to_file = './generate_scenes_request.json'
        with open(path_to_file, 'r', encoding='UTF8') as f:
            source_dict = json.load(f)

        result = conv.parse_scene_generation_data_from_dict(source_dict)

        assert result is not None


if __name__ == '__main__':
    unittest.main()
