package aoc2023

import aoc2021b.size

fun main() = day11(generateSequence(::readlnOrNull).toList()).forEach(::println)

private fun day11(space: List<String>): List<Any?> {

    data class Galaxy(val x: Int, val y: Int)

    val galaxies = space.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c -> if (c == '#') Galaxy(x, y) else null }
    }.toSet()

    val doubleRows = space.indices.filter { y -> space[y].all { it == '.' } }
    val doubleColumns = space[0].indices.filter { x -> space.map { it[x] }.all { it == '.' } }

    val galaxyPairs = mutableSetOf<Set<Galaxy>>()
    var part1 = 0L
    var part2 = 0L

    for (a in galaxies) {
        for (b in galaxies) {
            if (!galaxyPairs.add(setOf(a, b))) continue

            val rx = intRangeUntil(a.x, b.x)
            val ry = intRangeUntil(a.y, b.y)

            val dx = doubleColumns.count { it in rx }
            val dy = doubleRows.count { it in ry }

            val dist = rx.size() + ry.size()

            part1 += dist + dx + dy
            part2 += dist + 999999 * dx + 999999 * dy
        }
    }

    return listOf(part1, part2)
}

private fun intRangeUntil(a: Int, b: Int) = listOf(a, b).sorted().let { (a, b) -> a until b }
