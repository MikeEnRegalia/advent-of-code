import fileinput
import string

ORIGINAL_MAZE = [list(row.strip()) for row in fileinput.input()]
MAZE: list[list[str]] = ORIGINAL_MAZE
for i in range(0, len(MAZE)):
    MAZE[i] = MAZE[i].copy()
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


def optimize():
    while True:
        changed = 0
        for y in range(0, len(MAZE)):
            for x in range(0, len(MAZE[0])):
                if MAZE[y][x] == '.':
                    s = 0
                    s += 1 if MAZE[y - 1][x] == '#' else 0
                    s += 1 if MAZE[y + 1][x] == '#' else 0
                    s += 1 if MAZE[y][x - 1] == '#' else 0
                    s += 1 if MAZE[y][x + 1] == '#' else 0
                    if s == 3:
                        MAZE[y][x] = '#'
                        changed += 1
        if changed == 0:
            break
    for r in MAZE:
        print("".join(r))


def collect_keys():
    optimize()

    def at(x, y, keys: tuple[str]):
        c: str = MAZE[y][x]
        if c == '#':
            return None
        if c in string.ascii_uppercase and c.lower() not in keys:
            return None
        if c in string.ascii_lowercase and c not in keys:
            return x, y, tuple(sorted([*keys, c]))
        return x, y, keys

    def neighbors(state):
        (x, y, keys) = state
        n = list()
        n.append(at(x + 1, y, keys))
        n.append(at(x - 1, y, keys))
        n.append(at(x, y + 1, keys))
        n.append(at(x, y - 1, keys))
        return list(filter(lambda ne: ne is not None, n))

    def walk():
        (ex, ey) = ENTRANCE

        pos = ex, ey, tuple()
        v = set()
        q = set()
        q.add(pos)
        d: dict = {pos: 0}

        while True:
            for n in [n for n in neighbors(pos) if n not in v]:
                q.add(n)
                dn = d[pos] + 1
                d[n] = dn if n not in d else min(d[n], dn)
            v.add(pos)
            if pos[2] == KEYS:
                print(d[pos])
                break
            q.remove(pos)
            f = sorted(q, key=lambda fe: d[fe])
            if len(f) == 0:
                break
            pos = f[0]

    walk()


collect_keys()

MAZE = ORIGINAL_MAZE
for i in range(0, len(MAZE)):
    MAZE[i] = MAZE[i].copy()


def part2():
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

    optimize()

    def at(x, y, keys: tuple[str]):
        c: str = MAZE[y][x]
        if c == '#':
            return None
        if c in string.ascii_uppercase and c.lower() not in keys:
            return None
        if c in string.ascii_lowercase and c not in keys:
            return x, y, tuple(sorted([*keys, c]))
        return x, y, keys

    def neighbors(state):
        (r1, r2, r3, r4, keys) = state
        n = list()
        for (x1n, y1n, nkeys) in neighbors1(r1, keys):
            n.append(((x1n, y1n), r2, r3, r4, nkeys))
        for (x2n, y2n, nkeys) in neighbors1(r2, keys):
            n.append((r1, (x2n, y2n), r3, r4, nkeys))
        for (x3n, y3n, nkeys) in neighbors1(r3, keys):
            n.append((r1, r2, (x3n, y3n), r4, nkeys))
        for (x4n, y4n, nkeys) in neighbors1(r4, keys):
            n.append((r1, r2, r3, (x4n, y4n), nkeys))
        return n

    def neighbors1(r, keys):
        (x, y) = r
        n = list()
        n.append(at(x + 1, y, keys))
        n.append(at(x - 1, y, keys))
        n.append(at(x, y + 1, keys))
        n.append(at(x, y - 1, keys))
        return list(filter(lambda ne: ne is not None, n))

    def walk():
        pos = (ex - 1, ey - 1), (ex + 1, ey - 1), (ex - 1, ey + 1), (ex + 1, ey + 1), tuple()
        v = set()
        q = set()
        q.add(pos)
        d: dict = {pos: 0}

        while True:
            for n in [n for n in neighbors(pos) if n not in v]:
                q.add(n)
                dn = d[pos] + 1
                d[n] = dn if n not in d else min(d[n], dn)
            v.add(pos)
            if pos[4] == KEYS:
                print(d[pos])
                break
            q.remove(pos)
            f = sorted(q, key=lambda fe: d[fe])
            if len(f) == 0:
                break
            pos = f[0]

    walk()


part2()
