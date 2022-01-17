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
        for x in range(min_x // f, max_x // f + 1):
            for y in range(min_y // f, max_y // f + 1):
                for z in range(min_z // f, max_z // f + 1):
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
            (px2, py2, pz2) = ((x - 1) * f, (y - 1) * f, (z - 1) * f)
            (px3, py3, pz3) = ((x + 1) * f, (y + 1) * f, (z + 1) * f)
            drill(f // 2, min_x=px2, max_x=px3, min_y=py2, max_y=py3, min_z=pz2, max_z=pz3)

    bots_x = [b[0] for b in bots]
    bots_y = [b[1] for b in bots]
    bots_z = [b[2] for b in bots]
    max_all = max(max(bots_x), max(bots_y), max(bots_z))

    lowest_f = 1
    while max_all // lowest_f > 0:
        lowest_f *= 2

    drill(lowest_f,
          min_x=min(bots_x), max_x=max(bots_x),
          min_y=min(bots_y), max_y=max(bots_y),
          min_z=min(bots_z), max_z=max(bots_z))

    total_max_n = max(r.keys())
    closest_points = sorted([manhattan(m) for m in r[total_max_n]])
    print(closest_points[0])


part2()
