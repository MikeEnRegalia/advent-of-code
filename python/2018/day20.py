import fileinput


regex = fileinput.input().readline()


class Maze:
    pos = 0
    point = (0, 0)
    doors = dict()

    def peek(self):
        return regex[self.pos]

    def read(self):
        r = self.peek()
        self.pos += 1
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
        next_point = None
        if c == "N":
            next_point = (self.point[0], self.point[1] - 1)
        elif c == "S":
            next_point = (self.point[0], self.point[1] + 1)
        elif c == "W":
            next_point = (self.point[0] - 1, self.point[1])
        elif c == "E":
            next_point = (self.point[0] + 1, self.point[1])
        self.doors.setdefault(self.point, set()).add(next_point)
        self.doors.setdefault(next_point, set()).add(self.point)
        self.point = next_point

    def points(self):
        return self.doors.keys()

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
