import fileinput


data = list(map(lambda x: x.removesuffix('\n'), fileinput.input()))


def freq(line):
    x = dict()
    for c in line:
        x[c] = x.setdefault(c, 0) + 1
    return set(x.values())


def aoc2021b.part1():
    sum_twice = 0
    sum_thrice = 0
    for f in map(lambda x: freq(x), data):
        if 2 in f:
            sum_twice += 1
        if 3 in f:
            sum_thrice += 1
    return sum_twice * sum_thrice


def aoc2021b.part2():
    for a in data:
        for b in data:
            r = ''
            for i in range(0, len(a)):
                if a[i] == b[i]:
                    r += a[i]
            if len(r) == len(a) - 1:
                return r
    return None


print(aoc2021b.part1())
print(aoc2021b.part2())
