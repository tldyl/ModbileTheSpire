import json
import sys
import os
import io
import pytest
from functools import partial
from util import get_loc_dir_path, read_json, flatten, get_json_files, get_loc_dirs

if sys.version_info.major < 3:
    raise Exception("must use python 3")


def fill_with_a(a, b):
    """ Resolves discrepencies between 'a' and 'b' with contents from 'a'. """
    assert type(a) == type(b), "type: a='{}', type='{}' vs b='{}', type='{}'".format(a, type(a), b, type(b))
    if isinstance(a, dict):
        missing_keys = a.keys() - b.keys()
        for key in missing_keys:
            b[key] = a.get(key)
        extra_keys = b.keys() - a.keys()
        if len(extra_keys) > 0:
            raise Exception("Uhoh, I don't know what to do with these extra "
                            "keys: '{}'. Someone might have screwed up?"
                            .format(extra_keys))
        assert len(a.keys()) == len(b.keys())
        return flatten([fill_with_a(x[0][1], x[1][1]) for x in zip(sorted(a.items()), sorted(b.items()))])
    elif isinstance(a, list):
        b.extend(a[len(b):])
        return flatten(map(lambda x: fill_with_a(*x), zip(a, b)))
    else:
        return []


def resolve_discrep(eng_files, other_files):
    eng_files = sorted(list(eng_files))
    other_files = sorted(list(other_files))
    eng_filenames = list(map(os.path.basename, eng_files))
    other_filenames = list(map(os.path.basename, other_files))
    assert eng_filenames == other_filenames, "{} --- {}".format(eng_filenames, other_filenames)
    eng_jsons = list(map(read_json, eng_files))
    non_eng_jsons = list(map(read_json, other_files))
    list(map(lambda x: fill_with_a(*x), zip(eng_jsons, non_eng_jsons)))
    list(map(lambda x: write_json(*x), zip(other_files, non_eng_jsons)))


def write_json(filename, data):
    with io.open(filename, 'w', encoding="utf-8") as f:
        json.dump(data, f, indent=2, ensure_ascii=False)


def main():
    eng_pack = os.path.join(get_loc_dir_path(), "eng")
    lang_packs = [x for x in get_loc_dirs() if not x.endswith("eng")]
    eng_json = get_json_files(eng_pack)
    other_jsons = filter(lambda x: x != [], [get_json_files(x) for x in lang_packs])
    resolve_w_eng = partial(resolve_discrep, eng_json)
    errors = list(map(resolve_w_eng, other_jsons))
    if any(errors):
        print("Errors: {}".format(errors))


if __name__ == "__main__":
    main()


@pytest.mark.parametrize("a,b,expect", [
    ({'c': 3, 'z': 0}, {'a': 1, 'z': 0}, {'a': 1, 'c': 3, 'z': 0}),
    ([], ['a', 'b', 'c'], ['a', 'b', 'c']),
    ([1, 2], [1, 2, 3], [1, 2, 3]),
    ([1, 2, 3], [1, 2, 3], [1, 2, 3]),
    ([1, 2, 3, 4], [1, 2, 3], [1, 2, 3, 4]),
    ([1, 2, 3, 4, 5], [1, 2, 3], [1, 2, 3, 4, 5]),
])
def test_compare(a, b, expect):
    fill_with_a(a, b)
    assert b == expect
