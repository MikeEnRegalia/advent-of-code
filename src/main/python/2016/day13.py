import fileinput

favorite_number = int(fileinput.input().readline())


def is_wall(p):
    (x, y) = p
    if x < 0 or y < 0:
        return True
    n = bin((x + y) * (x + y) + 3 * x + y + favorite_number)
    return len([i for i in n[2:] if i == '1']) % 2 == 1


def neighbors(x, y):
    return [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]


def walk(target, start=(1, 1)):
    pos = start
    v = set()
    d: dict = {pos: 0}
    while True:
        (x, y) = pos
        for n in neighbors(x, y):
            if not is_wall(n) and n not in v:
                distance = d[pos] + 1
                if n not in d or distance < d[n]:
                    d[n] = distance
        v.add(pos)
        next_nodes = sorted([n for n in d.keys() if n not in v], key=lambda p: d[p])
        if len(next_nodes) == 0:
            break
        pos = next_nodes[0]

    print(d[target])
    print(len([i for i in d.values() if i <= 50]))


walk((31, 39))
