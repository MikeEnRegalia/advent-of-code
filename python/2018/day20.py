import fileinput

REGEX = fileinput.input().readline()


def find_doors():
    regex_pos = 0
    point = (0, 0)
    doors = dict()

    def peek():
        return REGEX[regex_pos]

    def read():
        nonlocal regex_pos
        r = peek()
        regex_pos += 1
        return r

    def parse_group():
        nonlocal point
        initial_point = point
        assert read() in "^("
        while True:
            c = peek()
            if c in "NSEW":
                move()
            elif c == "|":
                point = initial_point
                read()
            elif c == "(":
                parse_group()
            else:
                assert c in ")$"
                read()
                break

    def move():
        nonlocal point
        c = read()

        (x, y) = (nx, ny) = point
        if c == "N":
            ny -= 1
        elif c == "S":
            ny += 1
        elif c == "W":
            nx -= 1
        elif c == "E":
            nx += 1
        else:
            raise c

        doors.setdefault((x, y), set()).add((nx, ny))
        doors.setdefault((nx, ny), set()).add((x, y))
        point = (nx, ny)

    parse_group()
    return doors


DOORS = find_doors()

curr = (0, 0)
D = {curr: 0}
Q = {curr}
V = set()
while True:
    for n in [n for n in DOORS[curr] if n not in V]:
        Q.add(n)
        d = D[curr] + 1
        if n not in D or D[n] > d:
            D[n] = d
    Q.remove(curr)
    V.add(curr)
    if len(Q) == 0:
        break
    curr = sorted(Q, key=lambda q: D[q])[0]

print(max(D.values()))
print(len([d for d in D.values() if d >= 1000]))
