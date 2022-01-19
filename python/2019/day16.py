import fileinput

signal = list(map(int, list(fileinput.input().readline().strip())))


def fft(data, offset=0):
    r = list()
    if offset != 0:
        sum_all = sum(data)
        for i in range(0, len(data)):
            r.append(sum_all)
            sum_all -= data[i]
    else:
        for i in range(0, len(data)):
            s = 0
            for i2 in range(i, len(data)):
                f = ((offset + i2 + 1) // (offset + i + 1)) % 4
                if f == 1:
                    s += data[i2]
                elif f == 3:
                    s -= data[i2]
            r.append(s)

    return r


def cycle(s, offset=0):
    transformed = s
    for i in range(0, 100):
        transformed = list(map(lambda q: abs(q) % 10, fft(transformed, offset)))
    return "".join(map(str, transformed[:8]))


def part1():
    print(cycle(signal))


def part2():
    offset = int("".join(map(str, signal[:7])))
    new_signal = signal[offset % len(signal):]
    while offset + len(new_signal) != len(signal) * 10_000:
        new_signal.extend(signal)

    print(cycle(new_signal, offset))


part1()
part2()
