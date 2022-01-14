import fileinput
import re

lines = list(fileinput.input())
parsed = [re.match(r"pos=<([-]?\d+),([-]?\d+),([-]?\d+)>, r=(\d+)", line).groups() for line in lines]
bots = [list(map(int, g)) for g in parsed]


def part1():
    (sx, sy, sz, sr) = sorted(bots, key=lambda a: a[3], reverse=True)[0]

    in_range = 0
    for (x, y, z, _) in bots:
        d = abs(sx - x) + abs(sy - y) + abs(sz - z)
        if d <= sr:
            in_range += 1

    print(in_range)


part1()
