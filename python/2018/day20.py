import fileinput

REGEX = fileinput.input().readline()


class Maze:
    regex_pos = 0
    point = (0, 0)
    doors = dict()

    def peek(self):
        return REGEX[self.regex_pos]

    def read(self):
        r = self.peek()
        self.regex_pos += 1
        return r

    def parse_group(self):
        initial_point = self.point
        assert self.read() in "^("
        while True:
            c = self.peek()
            if c in "NSEW":
                self.move()
            elif c == "|":
                self.point = initial_point
                self.read()
            elif c == "(":
                self.parse_group()
            else:
                assert c in ")$"
                self.read()
                break

    def move(self):
        c = self.read()

        (x, y) = (nx, ny) = self.point
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

        self.doors.setdefault((x, y), set()).add((nx, ny))
        self.doors.setdefault((nx, ny), set()).add((x, y))
        self.point = (nx, ny)

    def neighbors(self, a):
        return self.doors[a]


m = Maze()
m.parse_group()

curr = (0, 0)
D = {curr: 0}
Q = set()
Q.add(curr)
V = set()
while True:
    for n in m.neighbors(curr):
        if n in V:
            continue
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
