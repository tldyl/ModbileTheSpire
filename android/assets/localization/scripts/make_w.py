import json
import re
import sys
import os
from util import get_loc_dir_path


if sys.version_info.major < 3:
    raise Exception("must use python 3")
else:
    from json.decoder import JSONDecodeError


def to_w(s):
    if isinstance(s, str):
        return re.sub(r"[a-zA-Z0-9]", 'W', s)
    elif isinstance(s, list):
        return [to_w(x) for x in s]
    elif isinstance(s, dict):
        for k, v in s.items():
            s[k] = to_w(v)
        return s
    else:
        raise Exception("Something else? '{}'".format(type(s)))


if __name__ == "__main__":
    localization_dir = get_loc_dir_path()
    eng_lang_dir = os.path.join(localization_dir, 'eng')
    for filepath in os.listdir(eng_lang_dir):
        if filepath.endswith('.json'):
            eng_file = os.path.join(eng_lang_dir, filepath)
            with open(eng_file) as f:
                try:
                    data = json.load(f)
                except JSONDecodeError:
                    print("Error with reading file: {}".format(eng_file))
                    raise
            data = to_w(data)
            json_data = json.dumps(data, indent=4)
            www_lang_dir = os.path.join(os.path.dirname(eng_lang_dir), 'www')
            if not os.path.isdir(www_lang_dir):
                os.mkdir(www_lang_dir)

            filename = os.path.basename(filepath)
            with open(os.path.join(www_lang_dir, filename), 'w') as f:
                f.write(json_data)
            print(data)


