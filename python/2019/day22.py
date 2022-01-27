import fileinput


def big_shuffle(deck_size, instructions):
    def shuffle_id(g):
        return g

    def shuffle_rev(f):
        return lambda g: f(deck_size - g - 1)

    def shuffle_cut(pos, f):
        if pos < 0:
            pos = deck_size - abs(pos)
        at = deck_size - pos
        return lambda g: f(g + pos if g < at else g - at)

    def shuffle_inc(inc, f):
        def foo(g):
            j = 0
            n = 0
            while j != g:
                j = (j + inc) % deck_size
                n += 1
            return f(n)

        return foo

    s = shuffle_id
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
print(part1(6061))

part2 = big_shuffle(119315717514047, INSTRUCTIONS)
# print("shuffling")
# print(part2(2020))
# TIMES = 101741582076661
