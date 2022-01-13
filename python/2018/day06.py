import fileinput
import itertools
from collections import Counter
from dataclasses import dataclass


@dataclass(eq=True, frozen=True)
class Point:
    x: int
    y: int


coordinates = list(map(lambda _: Point(int(_[0]), int(_[1])), map(lambda _: _.split(",").fileinput.input())))
coordinates_x, coordinates_y = list(map(lambda _: _.x, coordinates)), list(map(lambda _: _.y, coordinates))
min_p = Point(min(coordinates_x), min(coordinates_y))
max_p = Point(max(coordinates_x), max(coordinates_y))
area = list(itertools.product(range(min_p.x, max_p.x + 1), range(min_p.y, max_p.y + 1)))


def part1():
    points = list()
    for (x, y) in area:
        best_c = None
        best = None
        tied = False
        infinite = None
        for c in coordinates:
            dist = abs(c.x - x) + abs(c.y - y)
            if best is None or dist < best:
                best = dist
                best_c = c
                tied = False
                infinite = c.x in (min_p.x, max_p.x) or c.y in (min_p.y, max_p.y)
            elif dist == best:
                tied = True
        if best is not None and not tied and not infinite:
            points.append(best_c)

    return Counter(points).most_common(1)[0][1]


print(part1())


def part2():
    count = 0
    for (x, y) in area:
        sum_d = sum(map(lambda c: abs(c.x - x) + abs(c.y - y), coordinates))
        if sum_d < 10000:
            count += 1
    return count


print(part2())
