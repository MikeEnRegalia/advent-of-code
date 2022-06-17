import fileinput


def run(state, i: list):
    (m, m_p, rel) = state
    o = list()
    i_pos = 0
    while True:
        instruction = str(m[m_p]).zfill(5)
        modes = instruction[0:3][::-1]
        opcode = int(instruction[3:])

        def loc(p):
            return m[m_p + p] if modes[p - 1] == "0" else rel + m[m_p + p]

        def read(p):
            return m[m_p + p] if modes[p - 1] == "1" else m.setdefault(loc(p), 0)

        def write(p, v):
            m[loc(p)] = v

        if opcode == 1:
            write(3, read(1) + read(2))
            m_p += 4
        elif opcode == 2:
            write(3, read(1) * read(2))
            m_p += 4
        elif opcode == 3:
            write(1, i[i_pos])
            i_pos += 1
            m_p += 2
        elif opcode == 4:
            o.append(read(1))
            m_p += 2
        elif opcode == 5:
            m_p = read(2) if read(1) != 0 else m_p + 3
        elif opcode == 6:
            m_p = read(2) if read(1) == 0 else m_p + 3
        elif opcode == 7:
            write(3, 1 if read(1) < read(2) else 0)
            m_p += 4
        elif opcode == 8:
            write(3, 1 if read(1) == read(2) else 0)
            m_p += 4
        elif opcode == 9:
            rel += read(1)
            m_p += 2
        elif opcode == 99:
            return (m, m_p, rel), o
        else:
            raise ValueError(instruction)


SPRINGDROID = {int(q): int(n) for (q, n) in enumerate(fileinput.input().readline().split(","))}

SPRINGSCRIPT_WALK = (
    "NOT A T\n"
    "NOT B J\n"
    "OR T J\n"
    "NOT C T\n"
    "OR T J\n"
    "AND D J\n"
    "WALK\n"
)

SPRINGSCRIPT_RUN = (
    "NOT A T\n"
    "NOT B J\n"
    "OR T J\n"
    "NOT C T\n"
    "OR T J\n"
    "AND D J\n"
    "OR H T\n"
    "AND H T\n"
    "OR E T\n"
    "AND T J\n"
    "RUN\n"
)

for s in [SPRINGSCRIPT_WALK, SPRINGSCRIPT_RUN]:
    (_, o) = run((SPRINGDROID.copy(), 0, 0), [ord(c) for c in list(s)])
    r = max(o)
    if r > 127:
        print(r)
    else:
        print(''.join([chr(i) for i in o]))
