import fileinput
import itertools

BUGS = set()
WIDTH = HEIGHT = 0


def solve():
    global WIDTH, HEIGHT
    for (y, line) in enumerate(fileinput.input()):
        for (x, c) in enumerate(line.strip()):
            WIDTH = max(WIDTH, x + 1)
            HEIGHT = max(HEIGHT, y + 1)
            if c == '#':
                BUGS.add((x, y))

    evolved = BUGS
    bdr_h = set()
    while True:
        new = set()
        i = -1
        bdr = 0
        for (y, x) in itertools.product(range(0, HEIGHT), range(0, WIDTH)):
            i += 1
            n = 0
            for n0 in [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]:
                n += 1 if n0 in evolved else 0
            bug = True if (x, y) in evolved else False
            if bug and n != 1:
                bug = False
            elif not bug and n in [1, 2]:
                bug = True
            if bug:
                new.add((x, y))
                bdr += 2 ** i

        evolved = new

        if bdr in bdr_h:
            print(bdr)
            break
        bdr_h.add(bdr)


solve()


def solve2():
    levels = dict()
    levels[0] = BUGS.copy()

    for minute in range(0, 200):
        new_levels = dict()
        for (level, bugs) in levels:
            new = set()
            for (y, x) in itertools.product(range(0, HEIGHT), range(0, WIDTH)):
                n = 0
                for n0 in [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]:
                    n += 1 if n0 in bugs else 0
                bug = True if (x, y) in bugs else False
                if bug and n != 1:
                    bug = False
                elif not bug and n in [1, 2]:
                    bug = True
                if bug:
                    new.add((x, y))

            new_levels[level] = new
        levels = new_levels
