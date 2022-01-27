import fileinput


def big_shuffle(deck_size, instructions, init=lambda g: g):

    def shuffle_rev(f):
        return lambda g: f(deck_size - 1 - g)

    def shuffle_cut(pos, f):
        if pos < 0:
            pos = deck_size - abs(pos)
        at = deck_size - pos
        return lambda g: f(g + pos if g < at else g - at)

    def shuffle_inc(increment, f):
        def g(index):
            r = 0
            n = 0
            while r != index % increment:
                n += (deck_size - r) // increment + 1
                r = increment - (deck_size - r) % increment
            new_index = n + (index - r) // increment
            return f(new_index)

        return g

    s = init
    for ins in instructions:
        if ins == "deal into new stack":
            s = shuffle_rev(s)
        elif ins.startswith("cut "):
            s = shuffle_cut(int(ins[len("cut "):]), s)
        elif ins.startswith("deal with increment "):
            s = shuffle_inc(int(ins[len("deal with increment "):]), s)

    return s


INSTRUCTIONS = [line.strip() for line in fileinput.input()]

part1 = big_shuffle(10007, INSTRUCTIONS)
for i in range(10007):
    if part1(i) == 2019:
        print(i)

part2 = big_shuffle(119315717514047, INSTRUCTIONS)
c = 2020
h = {c: 0}
for i in range(101741582076661):
    if len(h) % 10000 == 0:
        print(f"{len(h)} ({c})")
    c = part2(c)
    if c in h:
        print(f"cycle: {c}, {i} -> {h[c]}")
        break
    else:
        h[c] = i
