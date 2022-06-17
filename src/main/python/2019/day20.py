import fileinput
import string
from collections import deque


def maze():
    r = dict()
    for (y, line) in enumerate(fileinput.input()):
        for (x, c) in enumerate(line):
            if c not in " \n":
                r[(x, y)] = c
    return r


def neighbors(p):
    (x, y) = p
    r = list()
    for i in [(x - 1, y), (x + 1, y), (x, y + 1), (x, y - 1)]:
        if i in MAZE:
            r.append((i, MAZE[i]))
    return r


def letter_neighbors(p):
    return [(p, c) for (p, c) in neighbors(p) if c in string.ascii_uppercase]


TRANSITIONS = dict()
TRANSITIONS_REC = dict()


def to_portal(c1, c2):
    return tuple(sorted([c1, c2]))


def adjacent_portal(p):
    (p, c1) = letter_neighbors(p)[0]
    (_, c2) = letter_neighbors(p)[0]
    return to_portal(c1, c2)


def portals():
    r = set()
    for (x, y) in MAZE.keys():
        if x in [MAZE_MIN_X, MAZE_MAX_X] or y in [MAZE_MIN_Y, MAZE_MAX_Y]:
            if MAZE[(x, y)] == '.':
                po = adjacent_portal((x, y))
                r.add(po)
                connect_portals((x, y), (po, 'o'))
    return r


def connect_portals(start, from_p):
    (from_portal, from_portal_dir) = from_p
    v = set()
    q = set()
    q.add(start)
    d: dict = {start: 0}
    p = start
    while True:
        for (np, nc) in [(np, nc) for (np, nc) in neighbors(p) if np not in v and nc == '.']:
            q.add(np)
            dist = d[p] + 1
            d[np] = dist if np not in d else min(d[np], dist)
        v.add(p)
        q.remove(p)
        ln = letter_neighbors(p)
        if len(ln) == 1:
            po = adjacent_portal(p)
            if po != from_portal:
                from_portal_is_o = from_portal_dir == 'o'
                po_is_o = p[0] in (MAZE_MIN_X, MAZE_MAX_X) or p[1] in (MAZE_MIN_Y, MAZE_MAX_Y)
                po_rec = (po, 'o' if po_is_o else 'i')
                TRANSITIONS_REC.setdefault((from_portal, from_portal_dir), []).append((po_rec, d[p] + 1))
                if po_rec[1] == 'i' and from_portal_is_o:
                    connect_portals(p, po_rec)

                TRANSITIONS.setdefault(from_portal, []).append((po, d[p] + 1))
        if len(q) == 0:
            break
        p = sorted(q, key=lambda a: d[a])[0]


MAZE = maze()
MAZE_MIN_X = min([k[0] for (k, v) in MAZE.items() if v == '#'])
MAZE_MAX_X = max([k[0] for (k, v) in MAZE.items() if v == '#'])
MAZE_MIN_Y = min([k[1] for (k, v) in MAZE.items() if v == '#'])
MAZE_MAX_Y = max([k[1] for (k, v) in MAZE.items() if v == '#'])
PORTALS = portals()


def walk():
    start = ('A', 'A')
    v = set()
    q = set()
    q.add(start)
    d = {start: 0}
    p = start
    while True:
        for (n, steps) in TRANSITIONS[p]:
            if n not in v:
                q.add(n)
                dist = d[p] + steps
                d[n] = dist if n not in d else min(d[n], dist)
        v.add(p)
        q.remove(p)
        if p == ('Z', 'Z'):
            print(d[p] - 1)
            break
        if len(q) == 0:
            break
        p = sorted(q, key=lambda a: d[a])[0]


walk()


def walk_rec():
    start = ((('A', 'A'), 'o'), 0)
    v = set()
    q = set()
    q.add(start)
    d: dict = {start: 0}
    p = start
    f: dict = {start: (None, 0)}
    while True:
        (p_portal, p_level) = p
        for ((n_portal, n_direction), steps) in [] if p_portal[0] == ('Z', 'Z') else TRANSITIONS_REC[p_portal]:
            n_level = p_level + (1 if n_direction == 'i' else -1)
            if n_portal in (('A', 'A'), ('Z', 'Z')) and n_level >= 0:
                continue
            if n_level < 0 and n_portal != ('Z', 'Z'):
                continue

            n_rec = ((n_portal, 'o' if n_direction == 'i' else 'i'), n_level)
            if n_rec not in v:
                q.add(n_rec)
                dist = d[p] + steps
                if n_rec not in d or dist < d[n_rec]:
                    d[n_rec] = dist
                    f[n_rec] = (p, steps)

        v.add(p)
        q.remove(p)
        if p_portal[0] == ('Z', 'Z'):
            u = p
            path = deque()
            s = 0
            while True:
                if u not in f:
                    break
                prev_u = u
                (u, dist) = f[u]
                s += dist
                path.appendleft(f"{''.join(prev_u[0][0])}: {dist - 1} + 1")
            print(f"{s-1}")
            break
        if len(q) == 0:
            break
        p = sorted(q, key=lambda a: d[a])[0]


walk_rec()
