import fileinput

data = list(map(int, fileinput.input()))

print(sum(data))

twice = None
h = set()
n = 0
while twice is None:
    for x in data:
        n += x
        if n in h:
            twice = n
            break
        h.add(n)
print(twice)
