import fileinput as fi

POLYMER = [c for c in fi.input().readline()]


def react(skip=None):
    r = [c for c in POLYMER if skip is None or c != skip and c != skip.upper()]
    i = 0
    while i < len(r):
        if i < len(r) - 1:
            a, b = r[i], r[i + 1]
            if a.lower() == b.lower() and a != b:
                del r[i:i + 2]
                if i > 0:
                    i -= 1
                continue
        i += 1
    return len(r)


print(react())


def improve():
    min_r = None
    for c in set(map(str.lower, POLYMER)):
        r = react(skip=c)
        if min_r is None or r < min_r:
            min_r = r
    return min_r


print(improve())
