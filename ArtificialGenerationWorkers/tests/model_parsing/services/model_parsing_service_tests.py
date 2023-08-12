import json
import unittest
import src.models.domain as models
import src.models.parsing as model_parsing
import dataclasses as dc

class ParseModelFromFile(unittest.TestCase):
    def test_parse_model_from_file(self):
        path_to_file = 'X:/projects/3D/test_objects/suzanne/suzanne.x3d'
        model: models.Model = model_parsing.import_model_from_file(path_to_file)

        model_asdict = dc.asdict(model)

        print(json.dumps(model_asdict, indent=4))


if __name__ == '__main__':
    unittest.main()
