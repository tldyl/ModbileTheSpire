import json
import csv
import sys
import os
import io
from functools import partial
from util import read_json, flatten, get_json_files, get_loc_dir_path

if sys.version_info.major < 3:
    raise Exception("must use python 3")


def create_csv_rows(a, b):
    """ Resolves discrepencies between 'a' and 'b' with contents from 'a'. """
    assert type(a) == type(b), "type: a='{}', type='{}' vs b='{}', type='{}'".format(a, type(a), b, type(b))
    a_flat = dict(flatten_dict(a))
    b_flat = dict(flatten_dict(b))
    assert len(a_flat.keys()) == len(b_flat.keys()), "UNBALANCED KEYS: {} != {}".format(a_flat.keys() - b_flat.keys(),
                                                                                        b_flat.keys() - a_flat.keys())
    rows = list(map(lambda k: (k, None, a_flat[k], b_flat[k]), a_flat.keys()))
    return rows


def flatten_dict(d, s=''):
    if isinstance(d, dict):
        return flatten(map(lambda k: flatten_dict(d[k], s + '.' + k), d.keys()))
    elif isinstance(d, list):
        return list(map(lambda v: (s + '.{0}'.format(v[0]), v[1]), enumerate(d)))
    else:
        return [(s, d)]


def process_files(eng_files, other_files):
    eng_files = sorted(list(eng_files))
    other_files = sorted(list(other_files))
    eng_filenames = list(map(os.path.basename, eng_files))
    other_filenames = list(map(os.path.basename, other_files))
    assert eng_filenames == other_filenames, "{} --- {}".format(eng_filenames, other_filenames)
    eng_jsons = list(map(read_json, eng_files))
    other_jsons = list(map(read_json, other_files))
    rows = list(map(lambda x: create_csv_rows(*x), zip(eng_jsons, other_jsons)))
    other_csv_filepaths = map(
        lambda p: os.path.join(os.path.dirname(p) + "_csv", os.path.basename(p).replace('.json', '.csv')), other_files)
    list(map(lambda x: write_csv(*x, header=("String ID", "Context", "English", "Japanese")),
             zip(other_csv_filepaths, rows)))  # TODO: refactor later. Too tired right now to do it.


def write_csv(filepath, data, header=None):
    if not os.path.exists(filepath):
        os.makedirs(os.path.dirname(filepath), exist_ok=True)
    with open(filepath, 'w', newline='', encoding='utf-8') as csv_file:
        writer = csv.writer(csv_file, delimiter='\t')
        if header:
            writer.writerow(header)
        for row in data:
            writer.writerow(row)


def write_json(filepath, data):
    with io.open(filepath, 'w', encoding="utf-8") as f:
        json.dump(data, f, indent=2, ensure_ascii=False)


def main():
    # We're just using japanese for csv creation for now. Put any other languages in here if needed later
    lang_packs = [os.path.join(get_loc_dir_path(), "jpn")]
    eng_pack = os.path.join(get_loc_dir_path(), "eng")

    eng_json = get_json_files(eng_pack)
    other_jsons = filter(lambda x: x != [], [get_json_files(x) for x in lang_packs])
    resolve_w_eng = partial(process_files, eng_json)
    errors = list(map(resolve_w_eng, other_jsons))
    if any(errors):
        print("Errors: {}".format(errors))


if __name__ == "__main__":
    main()
