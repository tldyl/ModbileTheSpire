import sys
import os
from functools import partial, reduce
from itertools import chain
from util import get_loc_dir_path, read_json, flatten, get_json_files, LintError, get_loc_dirs

# a list of files to be ignored.
ignored_files = []

if sys.version_info.major < 3:
    raise Exception("must use python 3")


class TypeMismatch(LintError):
    pass


class UnsharedDictKeys(LintError):
    pass


class ArraySizeMismatch(LintError):
    pass


def truncate_list(l):
    assert isinstance(l, list)
    return l if l == [] else "{}".format(l[0][:20])  # truncate


def compare(a, b, path):
    cmp = partial(compare, path=path)
    if type(a) != type(b):
        return [(path, TypeMismatch("'{}' and '{}'".format(a, b)))]
    elif isinstance(a, dict):
        a_keys = set(a.keys())
        b_keys = set(b.keys())
        set_diff = a_keys.symmetric_difference(b_keys)
        if set_diff != set():
            return [(path, UnsharedDictKeys("'{}'".format(set_diff)))]
        else:
            return flatten([cmp(x[0][1], x[1][1]) for x in zip(sorted(a.items()), sorted(b.items()))])
    elif isinstance(a, list):
        if len(a) != len(b):
            lang_pack = os.path.basename(os.path.dirname(path))
            return [(path, ArraySizeMismatch("(eng={} vs {}={}): search by '{}' or '{}' for eng or {} respectively"
                                             .format(len(a), lang_pack, len(b), truncate_list(a), truncate_list(b),
                                                     lang_pack)))]
        else:
            return flatten(map(lambda x: cmp(*x), zip(b, a)))
    else:
        return []


def compare_lang_pack(eng_files, other_files):
    eng_filenames = sorted(map(os.path.basename, eng_files))
    other_filenames = sorted(map(os.path.basename, other_files))
    assert eng_filenames == other_filenames, "{} --- {}".format(eng_filenames, other_filenames)
    eng_jsons = map(read_json, sorted(eng_files))
    non_eng_jsons = map(read_json, sorted(other_files))
    cmp_funcs = map(lambda p: partial(compare, path=p), sorted(other_files))
    return reduce(chain, map(lambda c, x: c(*x), cmp_funcs, zip(eng_jsons, non_eng_jsons)))


if __name__ == "__main__":
    localization_dir = get_loc_dir_path()
    eng_pack = os.path.join(localization_dir, "eng")
    lang_packs = [x for x in get_loc_dirs() if not x.endswith("eng")]
    eng_json = get_json_files(eng_pack)
    other_jsons = [get_json_files(x) for x in lang_packs]
    compare_against_eng = partial(compare_lang_pack, eng_json)
    errors = flatten(map(compare_against_eng, other_jsons))

    # Filter out errors in file ignore list. The more complicated rules are hardcoded to save time.
    errors = [e for e in errors if not (os.path.basename(e[0]) in ignored_files)]
    errors = [e for e in errors if not (os.path.basename(e[0]) in "keywords.json"
                                        and isinstance(e[1], ArraySizeMismatch))]
    errors = [e for e in errors if not (os.path.basename(e[0]) in "credits.json"
                                        and isinstance(e[1], ArraySizeMismatch))]
    errors = [e for e in errors if not (os.path.basename(e[0]) == "cards.json" and isinstance(e[1], UnsharedDictKeys)
                                        and 'UPGRADE_DESCRIPTION' in str(e[1]))]

    if len(errors) == 0:
        print("SUCCESS. No errors found.")
        sys.exit(0)
    else:
        error_count = len(errors)
        error_limit = 5
        if error_count > error_limit:
            trunc_msg = "(showing the first `{}`)".format(error_limit)
        else:
            trunc_msg = ""
        plurality = 's' if error_count > 1 else ''
        print("WARNING! `{}` error{} detected in localization json files {}:".format(error_count, plurality, trunc_msg))


        def error_format(err_list, limit):
            return list(map(lambda e: "In file: {}\n{}".format(e[0], e[1]), err_list[:limit]))


        msg = "```{}```".format("\n\n".join(error_format(errors, limit=error_limit)))
        print(msg)
        with open('validation_results.txt', 'w', encoding="utf-8") as f:
            f.write("\n\n".join(error_format(errors, limit=sys.maxsize)))
        sys.exit(1)
