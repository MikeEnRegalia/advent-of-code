import fileinput


def run(state, i):
    (m, pos, rel) = state
    m = m.copy()
    o = list()

    while True:
        instruction = str(m[pos]).zfill(5)
        modes = instruction[0:3][::-1]
        opcode = int(instruction[3:])

        def loc(p):
            return m[pos + p] if modes[p - 1] == "0" else rel + m[pos + p]

        def read(p):
            return m[pos + p] if modes[p - 1] == "1" else m.setdefault(loc(p), 0)

        def write(p, v):
            m[loc(p)] = v

        if opcode == 1:
            write(3, read(1) + read(2))
            pos += 4
        elif opcode == 2:
            write(3, read(1) * read(2))
            pos += 4
        elif opcode == 3:
            if i is None:
                return (m, pos, rel), o
            write(1, i)
            i = None
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
            return (m, pos, rel), o
        else:
            raise ValueError(instruction)


PROGRAM = {int(q): int(n) for (q, n) in enumerate(fileinput.input().readline().split(","))}


def walk():
    pos = (0, 0)
    V = set()
    Q: set = {pos}
    D: dict = {pos: 0}
    S: dict = dict()
    N: dict = dict()
    target = None
    state = (PROGRAM.copy(), 0, 0)

    def neighbor(p, cmds):
        nonlocal state, target
        (x, y) = p
        pos2 = (x + (0 if cmds[0] in [1, 2] else (1 if cmds[0] == 4 else -1)),
                y + (0 if cmds[0] in [3, 4] else (1 if cmds[0] == 2 else -1)))
        (state, o) = run(state, cmds[0])
        if o == [0]:
            return None
        S[pos2] = state
        (state, _) = run(state, cmds[1])
        if o == [2]:
            target = pos2
        return pos2

    def neighbors(p):
        r = list()
        r.append(neighbor(p, (1, 2)))
        r.append(neighbor(p, (2, 1)))
        r.append(neighbor(p, (3, 4)))
        r.append(neighbor(p, (4, 3)))
        return [n for n in r if n is not None]

    def find_target():
        nonlocal pos, state
        while True:
            S[pos] = state
            for n in N.setdefault(pos, neighbors(pos)):
                if n in V:
                    continue
                Q.add(n)
                if n not in D:
                    D[n] = D[pos] + 1
                else:
                    D[n] = min(D[n], D[pos] + 1)
            Q.remove(pos)
            f = sorted(Q, key=lambda q: -D[q])
            if len(f) == 0:
                break
            pos = f[0]
            state = S[pos]
            V.add(pos)

    find_target()
    print(D[target])

    def fill_with_oxygen():
        nonlocal pos, state
        pos = target
        V.clear()
        Q.clear()
        D.clear()
        Q.add(pos)
        D[pos] = 0

        pos = target
        while True:
            S[pos] = state
            for n in N[pos]:
                if n in V:
                    continue
                Q.add(n)
                if n not in D:
                    D[n] = D[pos] + 1
                else:
                    D[n] = min(D[n], D[pos] + 1)
            Q.remove(pos)
            f = sorted(Q, key=lambda q: -D[q])
            if len(f) == 0:
                break
            pos = f[0]
            state = S[pos]
            V.add(pos)

    fill_with_oxygen()
    print(max(D.values()))


walk()
