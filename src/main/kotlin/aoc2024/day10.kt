package aoc2024

import kotlin.math.min

fun main() {
    val grid = generateSequence(::readLine).map { it.map { it.digitToInt() } }.toList()

    data class Point(val x: Int, val y: Int) {
        fun neighbors() = sequenceOf(copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1))
        fun content() = grid.getOrNull(y)?.getOrNull(x)
        override fun toString(): String {
            return "($x,$y:${content()})"
        }
    }

    val starts = sequence {
        for (y in grid.indices) for (x in grid[y].indices)
            if (grid[y][x] == 0) yield(Point(x, y))
    }

    var part1 = 0
    var part2 = 0
    for (start in starts) {
        val found = mutableSetOf<List<Point>>()
        val visited = mutableSetOf(listOf(start))
        val distances = mutableMapOf(listOf(start) to 0)
        var curr = listOf(start)
        while (true) {
            curr.last().neighbors()
                .filter { it.content() == curr.last().content()!! + 1 }
                .map { curr + it }
                .filter { it !in visited }
                .forEach { neighbor ->
                    distances.compute(neighbor) { _, v -> min(distances.getValue(curr) + 1, v ?: Int.MAX_VALUE) }
                }
            curr = distances.keys.filter { it !in visited }.minByOrNull { distances[it]!! } ?: break
            visited += curr
            if (curr.last().content() == 9) found += curr
        }
        part1 += found.map { it.last() }.distinct().size
        part2 += found.size
    }

    println(part1)
    println(part2)
}
