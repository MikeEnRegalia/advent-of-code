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
    attack: str
    damage: int
    initiative: int

    def effective_power(self):
        return self.units * self.damage

    def damage_to(self, other):
        if self.attack in other.immune:
            return 0
        if self.attack in other.weak:
            return self.effective_power() * 2
        return self.effective_power()

    def hit_by(self, group):
        (units_killed, _) = divmod(group.damage_to(self), self.hp_per_unit)
        self.units = max(0, self.units - units_killed)


PATTERN = r"^(\d+) .* (\d+) hit points (\((weak to ([^;]+)(; )?)?(immune to (.+))?\) )?with .* (\d+) (\w+) .* (\d+)$"


def parse():
    army = None
    result = list()
    id = 0
    for line in [line.strip() for line in fileinput.input()]:
        if ":" in line:
            army = 'immune system' if line == 'Immune System:' else 'infection'
            continue
        elif line.strip() != '':
            (size, hp, _, _, weaknesses, _, _, immunities, damage, attack, initiative) = re.match(
                PATTERN,
                line).groups()
            weak = list() if weaknesses is None else weaknesses.split(", ")
            immune = list() if immunities is None else immunities.split(", ")
            result.append(Group(id, army, int(size), int(hp), weak, immune, attack, int(damage), int(initiative)))
            id += 1
    return result


P = parse()


def highest_initiative_groups():
    return sorted(P, key=lambda g: g.initiative, reverse=True)


def target_selection_groups():
    return sorted(highest_initiative_groups(), key=lambda g: g.effective_power(), reverse=True)


def select_target(group, already_attacked):
    targets = sorted([p for p in highest_initiative_groups() if p not in already_attacked and p.army != group.army],
                     key=lambda g: group.damage_to(g), reverse=True)
    return None if len(targets) == 0 else targets[0]


def fight():
    while True:
        units_before = sum([g.units for g in P])

        attacks = dict()
        for group in [g for g in target_selection_groups() if g.units > 0]:
            target = select_target(group, attacks.values())
            attacks[group.id] = target

        for group in [g for g in highest_initiative_groups() if g.units > 0]:
            target = attacks[group.id]
            if target is not None:
                target.hit_by(group)

        units_after = sum([g.units for g in P])
        if units_before == units_after:
            print(units_after)
            break


fight()
