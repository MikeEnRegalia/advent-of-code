import fileinput
import string

MAZE: list[list[str]] = [list(row.strip()) for row in fileinput.input()]
KEYS = set()


def init():
    entrance = 0, 0
    for (y, row) in enumerate(MAZE):
        for (x, c) in enumerate(row):
            if c in string.ascii_lowercase:
                KEYS.add(c)
            if c == '@':
                entrance = x, y
    return entrance


ENTRANCE = init()
KEYS = tuple(sorted(KEYS))


def solve(aoc2021b.part1):
    if aoc2021b.part1:
        start = tuple([ENTRANCE]), tuple()
    else:
        (ex, ey) = ENTRANCE
        MAZE[ey - 1][ex - 1] = '@'
        MAZE[ey - 1][ex + 1] = '@'
        MAZE[ey + 1][ex - 1] = '@'
        MAZE[ey + 1][ex + 1] = '@'
        MAZE[ey][ex - 1] = '#'
        MAZE[ey][ex + 1] = '#'
        MAZE[ey - 1][ex] = '#'
        MAZE[ey + 1][ex] = '#'
        MAZE[ey][ex] = '#'
        start = tuple([(ex - 1, ey - 1), (ex + 1, ey - 1), (ex - 1, ey + 1), (ex + 1, ey + 1)]), tuple()

    v = set()
    v3 = set()

    def at(positions, x, y, keys: tuple[str], prev, rec):
        if (x, y) in v3:
            return None
        c: str = MAZE[y][x]
        if c == '#' or c in string.ascii_uppercase and c.lower() not in keys:
            return None
        if c in string.ascii_lowercase and c not in keys:
            return x, y, tuple(sorted([*keys, c])), 1
        if not rec:
            return x, y, keys, 1
        v3.clear()
        return follow_dead_end((x, y), prev, keys, positions, 1)

    def follow_dead_end(n, prev, keys, positions, n_dist):
        v3.add(n)
        f: list = neighbors1(positions, n, keys, prev, False)
        if aoc2021b.part1 and n_dist == 1 and len(f) > 1:
            return n[0], n[1], keys, n_dist

        f = [j if j[2] != keys else follow_dead_end((j[0], j[1]), n, j[2], positions, n_dist) for j in f]
        f = [j for j in f if j is not None]
        if len(f) == 0:
            return None
        if len(f) > 1:
            return n[0], n[1], keys, n_dist
        (rx, ry, r_keys, r_dist) = f[0]
        return rx, ry, r_keys, n_dist + r_dist

    def neighbors1(positions, pos, keys, skip, rec):
        (x, y) = pos
        n = list()
        for (x2, y2) in [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]:
            if skip is None or skip != (x2, y2):
                n.append(at(positions, x2, y2, keys, pos, rec))
        return list(filter(lambda ne: ne is not None, n))

    def neighbors(state):
        (rs, keys) = state
        all_neighbors = list()
        for (ri, r) in enumerate(rs):
            for (xn, yn, nkeys, dist) in neighbors1(rs, r, keys, None, True):
                rl = list()
                for i in range(len(rs)):
                    rl.append((xn, yn) if i == ri else rs[i])
                all_neighbors.append(((tuple(rl), nkeys), dist))
        return all_neighbors

    def walk():
        pos = start
        q = {pos}
        d: dict = {pos: 0}

        max_keys = 0
        while True:
            for (n, dist) in [(n, dist) for (n, dist) in neighbors(pos) if n not in v]:
                q.add(n)
                dn = d[pos] + dist
                d[n] = dn if n not in d else min(d[n], dn)
            v.add(pos)
            keys = pos[1]
            if len(keys) > max_keys:
                print(len(keys))
                max_keys = len(keys)
            if keys == KEYS:
                print(d[pos])
                break
            q.remove(pos)
            if len(q) == 0:
                break
            pos = sorted(q, key=lambda fe: d[fe])[0]

    walk()


solve(aoc2021b.part1=True)
solve(aoc2021b.part1=False)
