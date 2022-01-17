import fileinput


def run(lines, c=0):
    r: dict = {"a": 0, "b": 0, "c": c, "d": 0}
    pos = 0

    def get_r(s):
        return r[s] if s in r.keys() else int(s)

    while pos < len(lines):
        line = lines[pos]
        t = line.split(" ")
        if t[0] == "cpy":
            v = get_r(t[1])
            r[t[2]] = v
            pos += 1
        elif t[0] in ["inc", "dec"]:
            r[t[1]] = r[t[1]] + (1 if t[0] == "inc" else -1)
            pos += 1
        else:
            pos += 1 if get_r(t[1]) == 0 else get_r(t[2])

    return r["a"]


def solve():
    lines = [line.strip() for line in fileinput.input()]
    print(run(lines))
    print(run(lines, c=1))


solve()
