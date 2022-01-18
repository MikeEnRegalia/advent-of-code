import fileinput

PROGRAM = [int(x) for x in fileinput.input().readline().strip().split(",")]


def run(m, pos, i):
    o = list()

    while pos < len(m):
        instruction = str(m[pos]).zfill(5)
        modes = instruction[3:0:-1]

        def read(p):
            return m[m[pos + p]] if int(modes[p]) == 0 else m[pos + p]

        def write(p, v):
            m[m[pos + p]] = v

        opcode = int(instruction[3:])
        if opcode == 1:
            write(3, read(1) + read(2))
            pos += 4
        elif opcode == 2:
            write(3, read(1) * read(2))
            pos += 4
        elif opcode == 3:
            write(1, i)
            pos += 2
        elif opcode == 4:
            o.append(read(1))
            pos += 2
        elif opcode == 5:
            pos = read(2) if read(1) != 0 else pos + 3
        elif opcode == 6:
            pos = read(2) if read(1) == 0 else pos + 3
        elif opcode == 7:
            write(3, 1 if read(1) < read(2) else 0)
            pos += 4
        elif opcode == 8:
            write(3, 1 if read(1) == read(2) else 0)
            pos += 4
        elif opcode == 99:
            pos = len(m)
        else:
            raise ValueError(instruction)

    return m, pos, o


print(run(PROGRAM.copy(), 0, 1)[2])
print(run(PROGRAM.copy(), 0, 5)[2])
