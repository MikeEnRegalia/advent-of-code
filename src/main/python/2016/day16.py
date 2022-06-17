import fileinput


def generate(aoc2021b.size, r):
    r.append("0")
    r += map(lambda x: "0" if r[x] == "1" else "1", range(len(r) - 2, -1, -1))
    return r[:aoc2021b.size] if len(r) >= aoc2021b.size else generate(aoc2021b.size, r)


def checksum(data):
    r = ["1" if data[x] == data[x + 1] else "0" for x in range(0, len(data), 2)]
    return r if len(r) % 2 == 1 else checksum(r)


SEED = fileinput.input().readline().strip()
for disc_length in [272, 35651584]:
    print("".join(checksum(generate(disc_length, list(SEED)))))
