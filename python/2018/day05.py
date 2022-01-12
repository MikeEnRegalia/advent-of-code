import fileinput as fi
from string import ascii_lowercase

POLYMER = fi.input().readline()


def react(skip=None):
    r = [c for c in POLYMER if skip is None or c != skip and c != skip.upper()]
    i = 0
    while i < len(r):
        if i < len(r) - 1:
            a, b = r[i], r[i + 1]
            if a.lower() == b.lower() and a != b:
                del r[i:i + 2]
                if i > 0:
                    i = i - 1
                continue
        i = i + 1
    return len(r)


print(react(POLYMER))


def improve():
    min_r = None
    for c in ascii_lowercase:
        r = react(skip=c)
        if min_r is None or r < min_r:
            min_r = r
    print(min_r)


improve()
