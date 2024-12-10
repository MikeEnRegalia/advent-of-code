package aoc2024

import kotlin.math.min

fun main() {
    val area = generateSequence(::readLine).map { it.map { it.digitToInt() } }.toList()

    data class Location(val x: Int, val y: Int) {
        fun next() = sequenceOf(copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1))
            .filter { it.content() == content()?.plus(1) }

        fun content() = area.getOrNull(y)?.getOrNull(x)
    }

    val heads = sequence {
        for (y in area.indices) for (x in area[y].indices) if (area[y][x] == 0) yield(Location(x, y))
    }

    var part1 = 0
    var part2 = 0

    for (head in heads) {
        val trails = mutableSetOf<List<Location>>()
        val V = mutableSetOf(listOf(head))
        val D = mutableMapOf(listOf(head) to 0)
        var curr = listOf(head)
        while (true) {
            curr.last().next().map { curr + it }
                .filter { it !in V }
                .forEach { neighbor ->
                    D.compute(neighbor) { _, v -> min(D.getValue(curr) + 1, v ?: Int.MAX_VALUE) }
                }
            curr = D.keys.filter { it !in V }.minByOrNull { D[it]!! } ?: break
            V += curr
            if (curr.last().content() == 9) trails += curr
        }
        part1 += trails.map { it.last() }.distinct().size
        part2 += trails.size
    }

    println(part1)
    println(part2)
}
