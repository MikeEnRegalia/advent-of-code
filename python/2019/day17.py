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


ASCII = {int(q): int(n) for (q, n) in enumerate(fileinput.input().readline().split(","))}
(_, o) = run((ASCII, 0, 0), None)
grid = "".join([chr(i) for i in o]).strip().split("\n")
pos = (0, 0)
facing = '^'


def part1():
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


part1()


def part2():
    global pos, facing
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

    def print_grid():
        for y in range(0, len(grid)):
            for x in range(0, len(grid[0])):
                print(facing if (x, y) == pos else grid[y][x], end='')
            print()

    while True:
        if can_forward():
            forward()
        elif can_left():
            turn_left()
        elif can_right():
            turn_right()
        else:
            break

    print(len(commands))



part2()
