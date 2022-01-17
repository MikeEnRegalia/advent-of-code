import fileinput
import itertools

import re

parsed = [re.match(r"pos=<([-]?\d+),([-]?\d+),([-]?\d+)>, r=(\d+)", line).groups() for line in fileinput.input()]
bots = [list(map(int, g)) for g in parsed]


def manhattan(a, b=(0, 0, 0)):
    return abs(a[0] - b[0]) + abs(a[1] - b[1]) + abs(a[2] - b[2])


def part1():
    (sx, sy, sz, sr) = sorted(bots, key=lambda a: a[3], reverse=True)[0]

    in_range = 0
    for (x, y, z, _) in bots:
        d = manhattan((sx, sy, sz), (x, y, z))
        if d <= sr:
            in_range += 1

    print(in_range)


part1()


def part2():
    total_max_n: int | None = None
    best_p = set()

    def drill(f, min_x, max_x, min_y, max_y, min_z, max_z):
        nonlocal total_max_n
        max_n = None
        points = set()
        for (x, y, z) in itertools.product(range(min_x, max_x + 1), range(min_y, max_y + 1), range(min_z, max_z + 1)):
            n = 0
            for (bx, by, bz, br) in bots:
                d = manhattan((bx / f, by / f, bz / f), (x, y, z))
                if d <= (round((br / f) + 1) if f > 1 else br):
                    n += 1
            if max_n == n:
                points.add((x, y, z))
            elif n > 0 and (max_n is None or max_n < n):
                max_n = n
                points.clear()
                points.add((x, y, z))

        if f == 1:
            if total_max_n is None or max_n > total_max_n:
                total_max_n = max_n
                best_p.clear()
            if max_n == total_max_n:
                for p in points:
                    best_p.add(p)
            return

        for (x, y, z) in points:
            drill(f // 2,
                  min_x=(x - 1) * 2, max_x=(x + 1) * 2,
                  min_y=(y - 1) * 2, max_y=(y + 1) * 2,
                  min_z=(z - 1) * 2, max_z=(z + 1) * 2)

    (min_bx, max_bx) = (min([b[0] for b in bots]), max([b[0] for b in bots]))
    (min_by, max_by) = (min([b[1] for b in bots]), max([b[1] for b in bots]))
    (min_bz, max_bz) = (min([b[2] for b in bots]), max([b[2] for b in bots]))

    max_f = 1
    while max(max_bx, max_by, max_bz) // max_f > 0:
        max_f *= 2

    drill(max_f,
          min_x=min_bx // max_f, max_x=max_bx // max_f,
          min_y=min_by // max_f, max_y=max_by // max_f,
          min_z=min_bz // max_f, max_z=max_bz // max_f)

    print(sorted([manhattan(m) for m in best_p])[0])


part2()
