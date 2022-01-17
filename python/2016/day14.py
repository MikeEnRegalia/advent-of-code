import fileinput
from collections import deque
from hashlib import md5

salt = fileinput.input().readline()


def solve(stretch=0):
    i = -1
    data = deque()

    def append_hash():
        nonlocal i
        i += 1
        h = md5((salt + str(i)).encode("UTF-8")).hexdigest()
        for r in range(0, stretch):
            h = md5(h.encode("UTF-8")).hexdigest()
        data.append(h)

    def run():
        for n in range(0, 1001):
            append_hash()

        s = 0
        i2 = 0
        while True:
            h = data[0]
            for t in range(0, len(h) - 2):
                if h[t] == h[t + 1] == h[t + 2]:
                    for n in range(1, 1001):
                        h2 = data[n]
                        for t2 in range(0, len(h2) - 4):
                            if h2[t2] == h2[t2 + 1] == h2[t2 + 2] == h2[t2 + 3] == h2[t2 + 4] == h[t]:
                                s += 1
                                break
                    break
            if s == 64:
                print(i2)
                break

            append_hash()
            data.popleft()
            i2 += 1

    run()


solve()
solve(2016)
