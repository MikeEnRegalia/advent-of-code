package aoc2022

import kotlin.math.min

fun main() = day18(String(System.`in`.readAllBytes())).forEach(::println)

private fun day18(input: String): List<Any?> {
    data class Point(val x: Int, val y: Int, val z: Int) {
        fun neighbors() = sequenceOf(
            Point(x + 1, y, z),
            Point(x - 1, y, z),
            Point(x, y - 1, z),
            Point(x, y + 1, z),
            Point(x, y, z - 1),
            Point(x, y, z + 1)
        )
    }

    val cubes = input.lines().map { it.split(",").map(String::toInt).let { (x, y, z) -> Point(x, y, z) } }.toSet()

    val part1 = cubes.sumOf { p ->
        val (x, y, z) = p
        sequenceOf(
            Point(x + 1, y, z),
            Point(x - 1, y, z),
            Point(x, y - 1, z),
            Point(x, y + 1, z),
            Point(x, y, z - 1),
            Point(x, y, z + 1)
        ).filter { it !in cubes }.count()
    }

    val (minX, maxX) = cubes.minOf { it.x } to cubes.maxOf { it.x }
    val (minY, maxY) = cubes.minOf { it.y } to cubes.maxOf { it.y }
    val (minZ, maxZ) = cubes.minOf { it.z } to cubes.maxOf { it.z }

    var s = Point(minX - 1, minY - 1, minZ - 1)
    val air = mutableSetOf<Point>()
    val u = mutableSetOf<Point>()
    val d = mutableMapOf(s to 0)

    while (true) {
        s.neighbors()
            .filter { it !in cubes }
            .filter { it.x in minX - 1..maxX + 1 }
            .filter { it.y in minY - 1..maxY + 1 }
            .filter { it.z in minZ - 1..maxZ + 1 }
            .filter { it !in air }
            .forEach { n ->
                u += n
                val distance = d.getValue(s) + 1
                d.compute(n) { _, old -> min(distance, old ?: Int.MAX_VALUE) }
            }
        air += s
        u -= s
        s = u.minByOrNull { d.getValue(it) } ?: break
    }

    val part2 = cubes.sumOf { p ->
        val (x, y, z) = p
        sequenceOf(
            Point(x + 1, y, z),
            Point(x - 1, y, z),
            Point(x, y - 1, z),
            Point(x, y + 1, z),
            Point(x, y, z - 1),
            Point(x, y, z + 1)
        ).filter { it !in cubes && it in air }.count()
    }

    return listOf(part1, part2)
}

