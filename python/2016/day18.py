import fileinput


def transform_row(row):
    transformed = row.copy()
    for i in range(len(row)):
        left = False if i == 0 else row[i - 1]
        center = row[i]
        right = False if i == len(row) - 1 else row[i + 1]

        trap = False
        if left and center and not right or center and right and not left:
            trap = True
        elif left and not (center or right) or right and not (left or center):
            trap = True
        transformed[i] = trap

    return transformed


curr = [c == '^' for c in fileinput.input().readline()]
safe = 0
for turn in range(400000):
    for c in curr:
        if not c:
            safe += 1
    curr = transform_row(curr)
    if turn == 39:
        print(safe)
print(safe)
