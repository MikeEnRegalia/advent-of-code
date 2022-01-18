import fileinput


def run(m, pos, rel, i):
    o = list()

    while True:
        instruction = str(m[pos]).zfill(5)
        modes = instruction[0:3][::-1]
        opcode = int(instruction[3:])

        def loc(p):
            return m[pos + p] if modes[p-1] == "0" else rel + m[pos + p]

        def read(p):
            return m[pos + p] if modes[p-1] == "1" else m.setdefault(loc(p), 0)

        def write(p, v):
            m[loc(p)] = v

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
        elif opcode == 9:
            rel += read(1)
            pos += 2
        elif opcode == 99:
            return m, pos, rel, o
        else:
            raise ValueError(instruction)


PROGRAM = dict()
for (q, n) in enumerate(fileinput.input().readline().strip().split(",")):
    PROGRAM[q] = int(n)
print(run(PROGRAM.copy(), 0, 0, 1)[3])
print(run(PROGRAM.copy(), 0, 0, 2)[3])
