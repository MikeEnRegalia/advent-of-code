import fileinput
import itertools
from collections import Counter
from dataclasses import dataclass


@dataclass(eq=True, frozen=True)
class Point:
    x: int
    y: int


C = list()
X = list()
Y = list()
for line in fileinput.input():
    tokens = line.split(",")
    p = Point(int(tokens[0]), int(tokens[1]))
    C.append(p)
    X.append(p.x)
    Y.append(p.y)
MIN = Point(min(X), min(Y))
MAX = Point(max(X), max(Y))
A = list(map(lambda _: Point(_[0], _[1]), itertools.product(range(MIN.x, MAX.x + 1), range(MIN.y, MAX.y + 1))))


def dist(a, c):
    return abs(c.x - a.x) + abs(c.y - a.y)


def part1():
    points = list()
    for a in A:
        best_c = best = None
        for c in C:
            d = dist(a, c)
            if best is None or d < best:
                best = d
                infinite = c.x in (MIN.x, MAX.x) or c.y in (MIN.y, MAX.y)
                best_c = None if infinite else c
            elif d == best:
                best_c = None
        if best_c is not None:
            points.append(best_c)

    return Counter(points).most_common(1)[0][1]


print(part1())


def sum_dist(a):
    return sum(map(lambda c: dist(a, c), C))


print(len(list(filter(lambda s: s < 10000, map(sum_dist, A)))))
