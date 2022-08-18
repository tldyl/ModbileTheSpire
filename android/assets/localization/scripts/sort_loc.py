import json
import sys
import io
from util import read_json, flatten, get_json_files, get_loc_dirs


if sys.version_info.major < 3:
    raise Exception("must use python 3")


def write_json(filename, data):  # TODO: replace with util.write_json once sorting loc files is common.
    with io.open(filename, 'w', encoding="utf-8") as f:
        json.dump(data, f, indent=2, ensure_ascii=False, sort_keys=True)


def main():
    lang_packs = [x for x in get_loc_dirs()]
    json_files = flatten(map(get_json_files, lang_packs))
    for filepath in json_files:
        contents = read_json(filepath)
        write_json(filepath, contents)


if __name__ == "__main__":
    main()

