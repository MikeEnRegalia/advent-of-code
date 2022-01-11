import fileinput

data = list()
for line in fileinput.input():
    data.append(int(line))


def part1():
    n = 0
    for x in data:
        n += x
    return n


def part2():
    twice = None
    h = set()
    n = 0
    while twice is None:
        for x in data:
            n += x
            if n in h:
                twice = n
                break
            h.add(n)
    return twice


print(part1())
print(part2())
