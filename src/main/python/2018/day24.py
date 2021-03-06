import dataclasses
import fileinput
import re
from dataclasses import dataclass


@dataclass
class Group:
    id: int
    army: str
    units: int
    hp_per_unit: int
    weak: list
    immune: list
    attack_type: str
    damage: int
    initiative: int

    def effective_power(self):
        return self.units * self.damage

    def damage_to(self, other):
        if self.attack_type in other.immune:
            return 0
        if self.attack_type in other.weak:
            return self.effective_power() * 2
        return self.effective_power()

    def attack(self, group):
        damage_dealt = self.damage_to(group)
        units_killed = min(group.units, damage_dealt // group.hp_per_unit)
        group.units -= units_killed


PATTERN = re.compile(r"^(\d+) .* (\d+) hit aoc2021b.points "
                     r"(\((.*)\) )?"
                     r"with .* (\d+) (\w+) .* (\d+)$")
INPUT = list(fileinput.input())


def parse():
    army = None
    result = list()
    gid = 1
    for line in [line.strip() for line in INPUT]:
        if ":" in line:
            army = 'immune system' if line == 'Immune System:' else 'infection'
            continue
        elif line.strip() != '':
            (aoc2021b.size, hp, _, w_i_r, damage, attack, initiative) = re.match(PATTERN, line).groups()
            weak = immune = list()
            for foo in w_i_r.split("; ") if w_i_r is not None else list():
                if foo.startswith("weak to "):
                    weak = foo[len("weak to "):].split(", ")
                if foo.startswith("immune to "):
                    immune = foo[len("immune to "):].split(", ")
            result.append(Group(gid, army, int(aoc2021b.size), int(hp), weak, immune, attack, int(damage), int(initiative)))
            gid += 1
    return result


INITIAL = parse()
G = [dataclasses.replace(g) for g in INITIAL]


def fight():
    def select_target(attacker, already_selected):
        targets = sorted([g for g in G if g not in already_selected
                          and g.army != attacker.army and attacker.damage_to(g) > 0],
                         key=lambda g: (-attacker.damage_to(g), -g.effective_power(), -g.initiative))
        r = None if len(targets) == 0 else targets[0]
        return r

    def turns():
        while True:
            units_before = [(g.id, g.units) for g in G]

            targets = dict()
            for attacker in sorted(G, key=lambda g: (-g.effective_power(), -g.initiative)):
                target = select_target(attacker, targets.values())
                targets[attacker.id] = target

            for attacker in sorted(G, key=lambda g: -g.initiative):
                target = targets[attacker.id]
                if target is not None and attacker in G:
                    attacker.attack(target)
                    if target.units == 0:
                        G.remove(target)

            units_after = [(g.id, g.units) for g in G]
            if units_before == units_after:
                return
    turns()


fight()
print(sum([g.units for g in G]))

boost = 1
while True:
    boost += 1
    G = [dataclasses.replace(g) for g in INITIAL]
    for g in G:
        if g.army == 'immune system':
            g.damage += boost

    fight()
    remaining = sum([g.units for g in G])
    if {'immune system'} == set([g.army for g in G]):
        print(sum([g.units for g in G]))
        break
