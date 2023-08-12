import json
import unittest
import src.scenes.generation as gen


class GenerateScene(unittest.TestCase):
    def test_generate_scene_from_params(self):
        result: gen.SceneData
        source_data: dict
        path_to_file = './generate_scenes_request.json'
        with open(path_to_file, 'r', encoding='UTF8') as f:
            source_data = json.load(f)

        result = gen.generate_scene(source_data)

        print(result)

        assert result is not None


if __name__ == '__main__':
    unittest.main()
