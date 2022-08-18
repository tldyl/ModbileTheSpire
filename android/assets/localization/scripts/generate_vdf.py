import sys
import os
from validate_localization import read_json
os.chdir(os.path.dirname(os.path.realpath(__file__)))
sys.path.append(os.path.join("..", "..", "..", "release_scripts", "lib"))
import vdf
from util import get_loc_dirs

if sys.version_info.major < 3:
    raise Exception("must use python 3")

# Note: this is a achievement_ids of the localization achievements.json files to the Steam achievements vdf files.
# Usage:
#  1. Update this with the achievements id that Steam assigns in the order that the achievements are in the json.
#  2. Run `python3 generate_vdf.py`
#  3. Upload the vdf files in `vdf_files`

# This list must reflect the order of the achievements.json!
achievement_ids = [
    (1, 1),  # "Shrug It Off"
    (1, 2),  # "Purity"
    (1, 3),  # "Come At Me"
    (1, 4),  # "The Pact"
    (1, 5),  # "Adrenaline"
    (1, 6),  # "Powerful"
    (1, 7),  # "Jaxxed"
    (1, 8),  # "Impervious"
    (1, 9),  # "Barricaded"
    (1, 10),  # "Catalyst"
    (1, 11),  # "Plague"
    (1, 12),  # "Ninja"
    (1, 13),  # "Infinity"
    (1, 14),  # "You Are Nothing"
    (1, 15),  # "Perfect"
    (1, 16),  # "The Guardian"
    (1, 17),  # "The Ghost"
    (1, 18),  # "The Boss"
    (1, 19),  # "The Automaton"
    (1, 20),  # "The Collector"
    (1, 21),  # "The Champion"
    (1, 22),  # "The Crow"
    (1, 23),  # "The Shapes"
    (1, 24),  # "The Time Eater"
    (1, 25),  # "Ruby"
    (1, 27),  # "Emerald"
    None,  # Hidden
    (1, 29),  # "Who Needs Relics?"
    (1, 30),  # "Speed Climber"
    (7, 1),  # "Ascend 0"
    (7, 2),  # "Ascend 10"
    (1, 31),  # "Minimalist"
    (7, 0),  # "Ooh Donut!"
    (7, 3),  # "Sapphire"
    (7, 4),  # "Common Sense"
    (7, 5),  # "Focused"
    (7, 6),  # "Neon"
    None,  # "Energy is Overrated"
    (7, 7),  # "My Lucky Day"
    (7, 8),  # "The Transient"
    (7, 9),  # "Ascend 20"
    (7, 10),  # "Ruby+"
    (7, 11),  # "Emerald+"
    (7, 12),  # "Sapphire+"
    (7, 13),  # "The End?"
    (7, 14),  # "Eternal One"
    (7, 15),  # "Amethyst"
    (7, 16),  # "Amethyst+"
]
### DO NOT MODIFY BELOW THIS LINE


vdf_structure = {
    "lang": {
        "Language": "english",
        "Tokens": {}
    }
}


def write_vdf(filename, data):
    with open(filename, 'w', encoding='UTF-8-sig') as f:
        vdf.dump(data, f, pretty=True)


def encode_utf8(s):
    return str(s).encode('utf-8')


def json_to_vdf(json_filepath, vdf_filepath):
    print("\nConverting {}".format(json_filepath))
    json_data = read_json(json_filepath)
    names = json_data['AchievementGrid']['NAMES']
    descriptions = json_data['AchievementGrid']['TEXT']
    assert len(names) == len(descriptions), "There is an unequal number of names and descriptions!"
    assert len(achievement_ids) == len(names), \
        "The achievement_ids must contain the same number of times as the names and descriptions in achievements.json"
    tokens = [(_id, names[i], descriptions[i]) for i, _id in enumerate(achievement_ids) if _id is not None]
    skipped = [names[i] for i, _id in enumerate(achievement_ids) if id is None]
    try:
        print("Skipped the following with 'None' as their id: {}".format(skipped))
    except UnicodeEncodeError:  # for when Windows console can't handle unicode
        print("Skipped the following with 'None' as their id: {}".format(encode_utf8(skipped)))
    tokens = sorted(tokens)
    for t in tokens:
        vdf_structure['lang']['Tokens']["NEW_ACHIEVEMENT_{}_{}_NAME".format(t[0][0], t[0][1])] = t[1]
        vdf_structure['lang']['Tokens']["NEW_ACHIEVEMENT_{}_{}_DESC".format(t[0][0], t[0][1])] = t[2]
    write_vdf(vdf_filepath, vdf_structure)
    print("Wrote vdf to {}".format(vdf_filepath))


def main():
    lang_dirs = get_loc_dirs()
    file_to_convert = "achievements.json"
    for d in lang_dirs:
        filepath = os.path.join(d, file_to_convert)
        if not os.path.isfile(filepath):
            print("Warning! no {} in {}".format(file_to_convert, d))
        else:
            vdf_dir = "vdf_files"
            if not os.path.isdir(vdf_dir):
                os.mkdir(vdf_dir)
            vdf_filepath = os.path.join(vdf_dir, filepath.replace(os.sep, "_").replace("json", "vdf"))
            json_to_vdf(filepath, vdf_filepath)


if __name__ == "__main__":
    main()
