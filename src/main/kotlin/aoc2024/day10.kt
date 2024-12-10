package aoc2024

import kotlin.math.min

fun main() {
    val area = generateSequence(::readLine).map { it.map { it.digitToInt() } }.toList()

    data class Location(val x: Int, val y: Int) {
        fun next() = sequenceOf(copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1))
            .filter { it.height() == height()?.plus(1) }

        fun height() = area.getOrNull(y)?.getOrNull(x)
    }
    fun List<Location>.next() = last().next().map { this + it }

    val heads = sequence {
        for (y in area.indices) for (x in area[y].indices) if (area[y][x] == 0) yield(Location(x, y))
    }

    var part1 = 0
    var part2 = 0

    for (head in heads) {
        val trails = mutableSetOf<List<Location>>()
        var trail = listOf(head)
        val V = mutableSetOf(trail)
        val D = mutableMapOf(trail to 0)
        while (true) {
            trail.next().filter { it !in V }.forEach { neighbor ->
                D.compute(neighbor) { _, v -> min(D.getValue(trail) + 1, v ?: Int.MAX_VALUE) }
            }
            trail = D.filter { it.key !in V }.minByOrNull { it.value }?.key ?: break
            V += trail
            if (trail.last().height() == 9) trails += trail
        }
        part1 += trails.map { it.last() }.distinct().size
        part2 += trails.size
    }

    println(part1)
    println(part2)
}
