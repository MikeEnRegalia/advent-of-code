import fileinput
import re
from dataclasses import dataclass


@dataclass(eq=True)
class Group:
    size: int
    hp: int
    weak: list
    immune: list
    attack: str
    damage: int
    initiative: int


P1 = list()
P2 = list()
P = None
for line in [line.strip() for line in fileinput.input()]:
    if ":" in line:
        P = P1 if line == 'Immune System:' else P2
        continue
    elif line.strip() != '':
        (size, hp, _, _, weaknesses, _, _, immunities, damage, attack, initiative) = re.match(
            r"^(\d+) .* (\d+) hit points (\((weak to ([^;]+)(; )?)?(immune to (.+))?\) )?with .* (\d+) (\w+) .* (\d+)$",
            line).groups()
        weaknessList = list() if weaknesses is None else weaknesses.split(", ")
        immuneList = list() if immunities is None else immunities.split(", ")
        P.append(Group(size, hp, weaknessList, immuneList, attack, damage, initiative))

print(P1)
print()
print(P2)
