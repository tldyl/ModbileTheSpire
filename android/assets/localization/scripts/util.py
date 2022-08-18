""" A collection of utility functions. """
import os
import json
import sys
from json.decoder import JSONDecodeError


def get_loc_dir_path():
    """ Returns the localization directory. """
    dir_name = 'localization'
    full_path = os.path.abspath(os.path.dirname(__file__))
    assert dir_name in full_path
    return os.path.join(full_path.split(dir_name)[0], dir_name)


def get_loc_dirs():
    """ Returns all localization directories. """
    loc_dir = get_loc_dir_path()
    return [os.path.join(loc_dir, x) for x in os.listdir(loc_dir) if
            len(x) == 3 and
            os.path.isdir(os.path.join(loc_dir, x)) and
            x != 'www']


def flatten(list_of_lists):
    return [y for x in list_of_lists for y in x]


class LintError(Exception):
    def __str__(self):  # Override to include exception name in exception string
        return "{}: {}".format(self.__class__.__name__, self.args[0])


class DuplicateKeyError(LintError):
    pass


def dict_raise_on_duplicates(ordered_pairs):
    """ Reject duplicate keys. """
    d = {}
    for k, v in ordered_pairs:
        if k in d:
            raise DuplicateKeyError("%r" % (k,))
        else:
            d[k] = v
    return d


def read_json(json_file):
    with open(json_file, encoding='UTF-8-sig') as f:
        try:
            return json.load(f, object_pairs_hook=dict_raise_on_duplicates)
        except JSONDecodeError as e:
            print("WARNING! Invalid Json detected. Problem in file: `{}` at line `{}`, column `{}`"
                  .format(json_file, e.lineno, e.colno))
            print(e)
            sys.exit(1)
        except DuplicateKeyError as e:
            print("In file: " + json_file)
            print(e)
            sys.exit(1)


def get_json_files(lang_dir):
    return [os.path.join(lang_dir, f) for f in os.listdir(lang_dir) if f.endswith('.json')]


def jsonify(j):
    return json.dumps(j, ensure_ascii=True)


def test_get_loc_dir_path():
    assert os.path.isdir(get_loc_dir_path())
    assert get_loc_dir_path().endswith("localization")


def test_get_loc_dirs():
    assert any(map(lambda x: x.endswith('eng'), get_loc_dirs()))
    assert any(map(lambda x: x.endswith('jpn'), get_loc_dirs()))

