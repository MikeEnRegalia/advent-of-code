import fileinput
import re


def parse(s):
    return [int(x) for x in re.match(r"^Disc #\d+ has (\d+) positions; .* (\d+)\.$", s).groups()]


discs = list(map(parse, fileinput.input()))


def drop():
    ts = -1
    while True:
        ts += 1
        aligned = True
        for (i, (positions, pos_0)) in enumerate(discs):
            pos = (pos_0 + ts + i + 1) % positions
            if pos != 0:
                aligned = False
                break
        if aligned:
            print(ts)
            break


drop()
discs.append((11, 0))
drop()
