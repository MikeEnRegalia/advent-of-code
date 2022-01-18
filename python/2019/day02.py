import fileinput
import itertools

PROGRAM = [int(x) for x in fileinput.input().readline().strip().split(",")]


def run(noun, verb):
    m = PROGRAM.copy()
    m[1] = noun
    m[2] = verb

    def loc(p):
        return m[pos + p]

    def read(p):
        return m[loc(p)]

    def write(p, v):
        m[loc(p)] = v

    pos = 0
    while pos < len(m):
        opcode = m[pos]
        if opcode == 1:
            write(3, read(1) + read(2))
            pos += 4
        elif opcode == 2:
            write(3, read(1) * read(2))
            pos += 4
        elif opcode == 99:
            pos = len(m)

    return m[0]


def part2():
    for (noun, verb) in itertools.product(range(0, 99), range(0, 99)):
        if run(noun, verb) == 19690720:
            return 100 * noun + verb
    return None


print(run(12, 2))
print(part2())
