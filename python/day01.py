import fileinput
data = list()
for line in fileinput.input():
    data.append(int(line))

n = 0
for x in data:
    n += x
part1 = n

part2 = None
H = set()
n = 0
while part2 is None:
    for x in data:
        n += x
        if H.__contains__(n):
            part2 = n
            break
        H.add(n)

print(part1)
print(part2)
