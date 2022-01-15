import fileinput

S = [tuple(map(int, line.split(","))) for line in fileinput.input()]


def distance(a, b):
    return sum(map(lambda foo: abs(foo[0] - foo[1]), zip(a, b)))


C = list()
K = set()
for s in S:
    if s in K:
        continue
    constellation = set([o for o in S if o not in K and distance(s, o) <= 3])
    if len(constellation) <= 1:
        continue
    while True:
        added = False
        for c1 in constellation.copy():
            for other in [c2 for c2 in S if c2 not in constellation and distance(c1, c2) <= 3]:
                constellation.add(other)
                added = True
        if not added:
            break
    C.append(constellation)
    for c2 in constellation:
        K.add(c2)

print(len(C) + len(S) - len(K))
