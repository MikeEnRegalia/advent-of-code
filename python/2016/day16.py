import fileinput

SEED = list(fileinput.input().readline().strip())


def dragon(a: list):
    r = a.copy()
    r.append("0")
    r += map(lambda x: "0" if a[x] == "1" else "1", range(len(a) - 1, -1, -1))
    return r


def generate(size, r):
    r2 = dragon(r)
    return r2[:size] if len(r2) >= size else generate(size, r2)


def checksum(data):
    r = ["1" if data[x] == data[x + 1] else "0" for x in range(0, len(data), 2)]
    return r if len(r) % 2 == 1 else checksum(r)


for disc_length in [272, 35651584]:
    print("".join(checksum(generate(disc_length, SEED))))
