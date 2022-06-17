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
            if i_pos >= len(i):
                return (m, m_p, rel), o
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


NAT: tuple[int, int] | None = None


def queue(o):
    global NAT
    for oi in range(0, len(o), 3):
        c, x, y = o[oi], o[oi + 1], o[oi + 2]
        if c == 255:
            if NAT is None:
                print(y)
            NAT = x, y
            continue
        M.setdefault(c, list()).append((x, y))


def to_i(m):
    i = list()
    for (x, y) in m:
        i.append(x)
        i.append(y)
    i.append(-1)
    m.clear()
    return i


def run_c(n, i):
    c = C[n] if n in C else (NIC.copy(), 0, 0)
    (C[n], o) = run(c, i)
    queue(o)


NIC = {int(q): int(n) for (q, n) in enumerate(fileinput.input().readline().split(","))}
C = dict()
M = dict()


def solve():
    for c in range(50):
        m = M[c] if c in M else []
        run_c(c, [c, *(to_i(m))])

    nat_y_h = set()
    while True:
        for (c, m) in [m for m in M.items()]:
            if len(m) > 0:
                run_c(c, to_i(m))

        if sum([len(v) for v in M.values()]) == 0:
            (nx, ny) = NAT
            if ny in nat_y_h:
                print(ny)
                break
            nat_y_h.add(ny)
            queue([0, nx, ny])


solve()
