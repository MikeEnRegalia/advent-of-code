import fileinput
import itertools
import re
from dataclasses import dataclass
from functools import reduce


@dataclass
class Rect:
    n: int
    offset: tuple[int, int]
    size: tuple[int, int]

    def points(self):
        return itertools.product(range(self.offset[0], self.offset[0] + self.size[0]),
                                 range(self.offset[1], self.offset[1] + self.size[1]))


def rect(line):
    g = re.match(r"^#(\d+) @ (\d+),(\d+): (\d+)x(\d+)$", line).groups()
    return Rect(int(g[0]), (int(g[1]), int(g[2])), (int(g[3]), int(g[4])))


data = list(map(lambda _: rect(_), fileinput.input()))

M = dict()
for r in data:
    for (x, y) in r.points():
        M[(x, y)] = M.setdefault((x, y), 0) + 1

part1 = reduce(lambda acc, v: acc + (1 if v >= 2 else 0), M.values(), 0)
print(part1)

for r in data:
    overlaps = False
    for (x, y) in r.points():
        if M[(x, y)] > 1:
            overlaps = True
    if not overlaps:
        print(r.n)
