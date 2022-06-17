import fileinput
import itertools

ASCII = {int(q): int(n) for (q, n) in enumerate(fileinput.input().readline().split(","))}


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


def scan():
    (_, o) = run((ASCII.copy(), 0, 0), list())
    return "".join([chr(i) for i in o]).strip().split("\n")


pos = (0, 0)
facing = '^'
grid = scan()


def aoc2021b.part1():
    global pos, facing
    r = 0
    for y in range(1, len(grid) - 1):
        for x in range(1, len(grid[y]) - 1):
            if grid[y][x] in '<>v^':
                pos = (x, y)
                facing = grid[y][x]
            if grid[y][x] in '.X':
                continue
            if grid[y - 1][x] in '.X' or grid[y + 1][x] in '.X':
                continue
            if grid[y][x - 1] in '.X' or grid[y][x + 1] in '.X':
                continue
            r += x * y
    print(r)


aoc2021b.part1()


def aoc2021b.part2():
    global pos, facing, grid
    commands = list()

    def turn_left():
        global facing
        dirs = "<v>^"
        facing = dirs[(dirs.index(facing) + 1) % len(dirs)]
        commands.append("L")

    def turn_right():
        global facing
        dirs = ">v<^"
        facing = dirs[(dirs.index(facing) + 1) % len(dirs)]
        commands.append("R")

    def at(x, y):
        if x not in range(0, len(grid[0])):
            return None
        if y not in range(0, len(grid)):
            return None
        return grid[y][x]

    def can_forward():
        global pos
        (x, y) = pos
        if facing == '^':
            return at(x, y - 1) == '#'
        if facing == 'v':
            return at(x, y + 1) == '#'
        if facing == '<':
            return at(x - 1, y) == '#'
        if facing == '>':
            return at(x + 1, y) == '#'
        raise AssertionError

    def can_left():
        global pos
        (x, y) = pos
        if facing == '^':
            return at(x - 1, y) == '#'
        if facing == 'v':
            return at(x + 1, y) == '#'
        if facing == '<':
            return at(x, y + 1) == '#'
        if facing == '>':
            return at(x, y - 1) == '#'
        raise AssertionError

    def can_right():
        global pos
        (x, y) = pos
        if facing == '^':
            return at(x + 1, y) == '#'
        if facing == 'v':
            return at(x - 1, y) == '#'
        if facing == '<':
            return at(x, y - 1) == '#'
        if facing == '>':
            return at(x, y + 1) == '#'
        raise AssertionError

    def forward():
        global pos
        (x, y) = pos
        if facing == '^':
            pos = (x, y - 1)
        if facing == 'v':
            pos = (x, y + 1)
        if facing == '<':
            pos = (x - 1, y)
        if facing == '>':
            pos = (x + 1, y)
        if len(commands) == 0 or commands[-1] in ['L', 'R']:
            commands.append(1)
        else:
            commands[-1] += 1

    def to_ascii(data):
        return list(map(ord, list(",".join(map(str, data)) + "\n")))

    while True:
        if can_forward():
            forward()
        elif can_left():
            turn_left()
        elif can_right():
            turn_right()
        else:
            break

    segments = set()
    for i1 in range(0, len(commands) - 1):
        for i2 in range(i1 + 4, len(commands)):
            segment = tuple(commands[i1:i2])
            if len(to_ascii(segment)) > 21:
                continue
            c = 0
            for i in range(0, len(commands) - 4):
                if tuple(commands[i:i + len(segment)]) == segment:
                    c += 1
            if c > 1:
                segments.add(tuple(segment))

    for combination in itertools.product(segments, segments, segments):
        if combination[0] == combination[1] or combination[1] == combination[2]:
            continue
        r = list()
        i = 0
        n = False
        while i < len(commands):
            m = False
            for (si, s) in enumerate(combination):
                if tuple(commands[i:i + len(s)]) == s:
                    r.append(chr(ord('A') + si))
                    i += len(s)
                    m = True
                    break
            n = m
            if not n:
                break
        if not n:
            continue
        if 'A' not in r or 'B' not in r or 'C' not in r:
            continue

        commands_c = list()
        for c in r:
            commands_c.extend(combination[ord(c) - ord('A')])
        assert tuple(commands) == tuple(commands_c)

        ri = to_ascii(r)
        if len(ri) > 21:
            continue

        i = list()
        i.extend(ri)
        for s in combination:
            i.extend(to_ascii(s))
        i.append(ord('n'))
        i.append(ord('\n'))

        ASCII[0] = 2
        (_, dust) = run((ASCII.copy(), 0, 0), i)
        print("".join(map(lambda q: chr(q) if q < 100 else str(q), dust)))
        return


aoc2021b.part2()
