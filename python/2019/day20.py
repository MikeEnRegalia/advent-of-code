import fileinput
import string

MAZE = dict()
for (y, line) in enumerate(fileinput.input()):
    for (x, c) in enumerate(line):
        if c not in " \n":
            MAZE[(x, y)] = c

print(MAZE)

MAZE_MIN_X = min([k[0] for (k, v) in MAZE.items() if v == '#'])
MAZE_MAX_X = max([k[0] for (k, v) in MAZE.items() if v == '#'])
MAZE_MIN_Y = min([k[1] for (k, v) in MAZE.items() if v == '#'])
MAZE_MAX_Y = max([k[1] for (k, v) in MAZE.items() if v == '#'])


def neighbors(p):
    (x, y) = p
    r = list()
    for i in [(x - 1, y), (x + 1, y), (x, y + 1), (x, y - 1)]:
        if i in MAZE:
            r.append((i, MAZE[i]))
    return r


def letter_neighbors(p):
    return [(p, c) for (p, c) in neighbors(p) if c in string.ascii_uppercase]


PORTALS = set()
for (x, y) in MAZE.keys():
    if x in [MAZE_MIN_X, MAZE_MAX_X] or y in [MAZE_MIN_Y, MAZE_MAX_Y]:
        if MAZE[(x, y)] == '.':
            (p, c1) = letter_neighbors((x, y))[0]
            (p2, c2) = letter_neighbors(p)[0]
            PORTALS.add(c1 + c2)
            # follow (x, y) to find connected portals

print(PORTALS)
