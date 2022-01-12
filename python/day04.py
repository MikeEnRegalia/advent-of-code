import fileinput
import re
from collections import Counter


def guard(line):
    return list(re.match(r"^\[\d+-\d+-\d+ (\d+):(\d+)] ((Guard #(\d+) begins shift)|(wakes up)|(falls asleep))$",
                         line).groups())


data = list(map(guard, sorted(fileinput.input())))

guard_number = None
asleep_since = None
ASLEEP = dict()
for g in data:
    # TODO use match?
    if g[4] is not None:
        guard_number = int(g[4])
        asleep_since = None
        print(f"{guard_number} begins shift")
    if g[5] is not None:
        if asleep_since is not None:
            for minute in range(asleep_since, int(g[1])):
                ASLEEP.setdefault(guard_number, list()).append(minute)
        asleep_since = None
        print(f"{guard_number} wakes up")
    if g[6] is not None:
        asleep_since = int(g[1])
        print(f"{guard_number} falls asleep")

most_asleep_guard = sorted(ASLEEP.keys(), key=lambda _: len(ASLEEP[_]), reverse=True)[0]
most_asleep_minutes = ASLEEP[most_asleep_guard]
most_asleep_minute = Counter(most_asleep_minutes).most_common(1)[0][0]

print(most_asleep_guard * most_asleep_minute)

top_guard_minute = (None, None, None)
for (guard, minutes_asleep) in ASLEEP.items():
    (max_minute, times) = Counter(minutes_asleep).most_common(1)[0]
    if top_guard_minute[2] is None or top_guard_minute[2] < times:
        top_guard_minute = (guard, max_minute, times)

print(top_guard_minute[0] * top_guard_minute[1])