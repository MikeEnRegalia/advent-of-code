import fileinput

data = list()
for line in fileinput.input():
    data.append(line.removesuffix('\n'))


def part1():
    twice = 0
    thrice = 0
    for line in data:
        x = {}
        for c in line:
            x[c] = 1 if c not in x else x[c] + 1
        a = False
        b = False
        for c, v in x.items():
            if v == 2:
                a = True
            if v == 3:
                b = True
        if a: twice += 1
        if b: thrice += 1
    return twice * thrice


print(part1())

