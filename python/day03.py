import fileinput
import itertools
import re
from functools import reduce


def rect(line):
    g = list(map(int, re.match(r"^#(\d+) @ (\d+),(\d+): (\d+)x(\d+)$", line).groups()))
    return g[0], list(itertools.product(range(g[1], g[1] + g[3]), range(g[2], g[2] + g[4])))


data = list(map(rect, fileinput.input()))

M = dict()
for (n, points) in data:
    for p in points:
        M[p] = M.setdefault(p, 0) + 1

part1 = reduce(lambda acc, v: acc + (1 if v > 1 else 0), M.values(), 0)
print(part1)

for (n, points) in data:
    overlaps = False
    for p in points:
        if M[p] > 1:
            overlaps = True
    if not overlaps:
        print(n)
