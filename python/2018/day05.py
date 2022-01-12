import fileinput


polymer = list(fileinput.input())[0]

reacted = [char for char in polymer]
pos = 0
while pos < len(reacted):
    if pos < len(reacted) - 1:
        (a, b) = reacted[pos], reacted[pos + 1]
        if a.lower() == b.lower() and a != b:
            del reacted[pos:pos+2]
            if pos > 0:
                pos = pos - 1
            continue
    pos = pos + 1

print(len(reacted))
