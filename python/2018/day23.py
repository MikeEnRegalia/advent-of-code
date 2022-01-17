import fileinput

import re

parsed = [re.match(r"pos=<([-]?\d+),([-]?\d+),([-]?\d+)>, r=(\d+)", line).groups() for line in fileinput.input()]
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

bots_x = [b[0] for b in bots]
bots_y = [b[1] for b in bots]
bots_z = [b[2] for b in bots]


def part2():
    r = dict()
    cache = set()

    def drill(precision,
              min_x=min(bots_x), max_x=max(bots_x),
              min_y=min(bots_y), max_y=max(bots_y),
              min_z=min(bots_z), max_z=max(bots_z)):
        max_n = None
        points = set()
        for x in range(min_x // precision, max_x // precision + 1):
            for y in range(min_y // precision, max_y // precision + 1):
                for z in range(min_z // precision, max_z // precision + 1):
                    n = 0
                    for (bx, by, bz, br) in bots:
                        d = abs(bx / precision - x) + abs(by / precision - y) + abs(bz / precision - z)
                        if d <= (round((br / precision) + 1) if precision > 1 else br):
                            n += 1
                    if max_n == n:
                        points.add((x, y, z))
                    elif n > 0 and (max_n is None or max_n < n):
                        max_n = n
                        points.clear()
                        points.add((x, y, z))

        if precision == 1:
            s = r.setdefault(max_n, set())
            for p in points:
                s.add(p)
            return

        for (x, y, z) in sorted(points, key=lambda m: abs(m[0]) + abs(m[1]) + abs(m[2])):
            k = (precision, x, y, z)
            if k not in cache:
                cache.add(k)
                (px2, py2, pz2) = ((x - 1) * precision, (y - 1) * precision, (z - 1) * precision)
                (px3, py3, pz3) = ((x + 1) * precision, (y + 1) * precision, (z + 1) * precision)
                drill(precision // 2, min_x=px2, max_x=px3, min_y=py2, max_y=py3, min_z=pz2, max_z=pz3)

    seq = list()
    seq.append(1)
    while seq[len(seq) - 1] < min(max(bots_x), max(bots_y), max(bots_z)):
        seq.append(seq[len(seq) - 1] * 2)

    drill(seq[len(seq) - 1])

    total_max_n = max(r.keys())
    closest_points = sorted([abs(m[0]) + abs(m[1]) + abs(m[2]) for m in r[total_max_n]])
    print(closest_points[0])


part2()
