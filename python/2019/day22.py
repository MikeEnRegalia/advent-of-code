import fileinput

instructions = [line.strip() for line in fileinput.input()]

data = list()
for i in range(10007):
    data.append(i)

for instruction in instructions:
    if instruction == "deal into new stack":
        data = list(reversed(data))
    elif instruction.startswith("cut "):
        x = int(instruction[len("cut "):])
        if x < 0:
            x = len(data) + x
        data = data[x:] + data[:x]
    elif instruction.startswith("deal with increment "):
        x = int(instruction[len("deal with increment "):])
        new = [None] * len(data)
        p = 0
        for d in data:
            new[p] = d
            p = (p + x) % len(data)
        data = new

print(data.index(2019))

