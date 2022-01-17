import fileinput

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
    r = dict()

    def drill(f, min_x, max_x, min_y, max_y, min_z, max_z):
        max_n = None
        points = set()
        for x in range(min_x, max_x + 1):
            for y in range(min_y, max_y + 1):
                for z in range(min_z, max_z + 1):
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
            s = r.setdefault(max_n, set())
            for p in points:
                s.add(p)
            return

        for (x, y, z) in sorted(points, key=lambda m: manhattan(m)):
            drill(f // 2,
                  min_x=(x - 1) * 2, max_x=(x + 1) * 2,
                  min_y=(y - 1) * 2, max_y=(y + 1) * 2,
                  min_z=(z - 1) * 2, max_z=(z + 1) * 2)

    bots_x = [b[0] for b in bots]
    bots_y = [b[1] for b in bots]
    bots_z = [b[2] for b in bots]
    max_all = max(max(bots_x), max(bots_y), max(bots_z))

    max_f = 1
    while max_all // max_f > 0:
        max_f *= 2

    drill(max_f,
          min_x=min(bots_x) // max_f, max_x=max(bots_x) // max_f,
          min_y=min(bots_y) // max_f, max_y=max(bots_y) // max_f,
          min_z=min(bots_z) // max_f, max_z=max(bots_z) // max_f)

    total_max_n = max(r.keys())
    closest_points = sorted([manhattan(m) for m in r[total_max_n]])
    print(closest_points[0])


part2()
