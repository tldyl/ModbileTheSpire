# coding=utf-8
# This script scans for characters outside the unicode BMP which would result
# in being encoded as two characters in UTF-16 which is what Java uses by default.
# To use: find . -name "*.json" | xargs -I{} python3 outside_bmp.py {}
import sys

if sys.version_info.major < 3:
        raise Exception("must use python 3")

bmp_limit = 65536


def main():
    filename = sys.argv[1]
    lines = read_lines(filename)
    for line in lines:
        process_chars(line)


def process_chars(s):
    for c in s:
        in_bmp = ord(c) < bmp_limit
        if not in_bmp:
            print("char={} cp={} inBMP={}".format(c, ord(c), in_bmp))


def read_lines(filename):
    with open(filename, encoding='UTF-8-sig') as f:
        return f.readlines()


if __name__ == "__main__":
    main()
