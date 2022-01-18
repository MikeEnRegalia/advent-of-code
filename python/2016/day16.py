import fileinput


def generate(size, r):
    r.append("0")
    r += map(lambda x: "0" if r[x] == "1" else "1", range(len(r) - 2, -1, -1))
    return r[:size] if len(r) >= size else generate(size, r)


def checksum(data):
    r = ["1" if data[x] == data[x + 1] else "0" for x in range(0, len(data), 2)]
    return r if len(r) % 2 == 1 else checksum(r)


SEED = fileinput.input().readline().strip()
for disc_length in [272, 35651584]:
    print("".join(checksum(generate(disc_length, list(SEED)))))
