import fileinput

data = map(lambda x: x.removesuffix('\n'), fileinput.input())


def freq(line):
    x = dict()
    for c in line:
        x[c] = 1 if c not in x else x[c] + 1
    return set(x.values())


def part1():
    sum_twice = 0
    sum_thrice = 0
    for f in map(lambda x: freq(x), data):
        if 2 in f:
            sum_twice += 1
        if 3 in f:
            sum_thrice += 1

    return sum_twice * sum_thrice


print(part1())
