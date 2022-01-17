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
    visited = set()
    distances: dict = {pos: 0}
    while True:
        (x, y) = pos
        for neighbor in [p for p in neighbors(x, y) if not is_wall(p) and p not in visited]:
            distance = distances[pos] + 1
            if neighbor not in distances or distance < distances[neighbor]:
                distances[neighbor] = distance
        visited.add(pos)
        next_nodes = sorted([i for i in distances.keys() if i not in visited], key=lambda p: distances[p])
        if len(next_nodes) == 0:
            break
        pos = next_nodes[0]

    print(distances[target])
    print(len([k for (k, visited) in distances.items() if visited <= 50]))


walk((31, 39))
