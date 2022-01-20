import fileinput
import string

MAZE = [list(row.strip()) for row in fileinput.input()]
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
print(KEYS)


def collect_keys():
    def optimize():
        while True:
            changed = 0
            for y in range(0, len(MAZE)):
                for x in range(0, len(MAZE[0])):
                    if MAZE[y][x] == '.' and \
                            MAZE[y - 1][x] in ".#" and MAZE[y + 1][x] in ".#" and \
                            MAZE[y][x - 1] in ".#" and MAZE[y][x + 1] in ".#":
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

    optimize()

    def at(x, y, keys: tuple[str]):
        c = MAZE[y][x]
        if c == '#':
            return None
        if c in string.ascii_uppercase and c.lower() not in keys:
            return None
        if c in string.ascii_lowercase and c not in keys:
            return x, y, tuple(sorted([*keys, c]))
        return x, y, keys

    def neighbors(state):
        (x, y, keys) = state
        r = list()
        r.append(at(x + 1, y, keys))
        r.append(at(x - 1, y, keys))
        r.append(at(x, y + 1, keys))
        r.append(at(x, y - 1, keys))
        return list(filter(lambda n: n is not None, r))

    def walk():
        (ex, ey) = ENTRANCE

        pos = ex, ey, tuple()
        v = set()
        q = set()
        q.add(pos)
        d: dict = {pos: 0}

        max_keys_seen = None
        while True:
            for n in [n for n in neighbors(pos) if n not in v]:
                q.add(n)
                dn = d[pos] + 1
                d[n] = dn if n not in d else min(d[n], dn)
            v.add(pos)
            if pos[2] == KEYS:
                print(d[pos])
                break
            else:
                keys_seen = len(pos[2])
                if max_keys_seen is None or keys_seen > max_keys_seen:
                    max_keys_seen = keys_seen
                    print(keys_seen)
            q.remove(pos)
            f = sorted(q, key=lambda i: d[i])
            if len(f) == 0:
                break
            pos = f[0]

    walk()


collect_keys()
