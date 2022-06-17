package aoc2021b

solve(generateSequence(::readLine).toList()).forEach(::println)

data class Pos(val x: Int, val y: Int) {
    fun neighbors() = sequence {
        for (x2 in x - 1..x + 1)
            for (y2 in y - 1..y + 1) if (x != x2 || y != y2) yield(copy(x = x2, y = y2))
    }
}

fun solve(lines: List<String>): List<Any?> {
    val cave = mutableMapOf<Pos, Int>().apply {
        for ((y, line) in lines.withIndex())
            line.map(Char::digitToInt).forEachIndexed { x, b -> this[Pos(x, y)] = b }
    }

    var part1 = 0
    var part2 = 0
    while (true) {
        ++part2
        cave.replaceAll { _, u -> u + 1 }
        val flashed = cave.flashAll().size
        if (part2 <= 100) part1 += flashed
        if (flashed == cave.size) break
      }

    return listOf(part1 to part2)
}

fun MutableMap<Pos, Int>.flashAll(): MutableSet<Pos> {
    val flashed = mutableSetOf<Pos>()
    fun flash(octopus: Pos) {
        flashed += octopus
        this[octopus] = 0
        octopus.neighbors().filterNot { it in flashed }.forEach { computeIfPresent(it) { _, energy -> energy + 1 } }
    }

    while (true) filter { it.value > 9 }.firstNotNullOfOrNull { it.key }?.let { flash(it) } ?: break
    return flashed
}