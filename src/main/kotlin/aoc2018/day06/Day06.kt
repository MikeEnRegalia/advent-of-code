package aoc2018.day06

import kotlin.math.abs

fun main() {
    val points = generateSequence(::readLine)
        .map { row -> row.split(", ".toRegex()).let { Point(it[0].toInt(), it[1].toInt()) } }
        .toList()

    val (min, max) = with(points) {
        Point(minOf { it.x }, minOf { it.y }) to Point(maxOf { it.x }, maxOf { it.y })
    }

    allSurrounding(min, max)
        .map { p -> points.filter { center -> p.belongsTo(center, points) } }
        .mapNotNull { it.getOrNull(0) }
        .groupingBy { it }.eachCount()
        .entries.maxByOrNull { it.value }.also { println(it) }

    allSurrounding(min, max)
        .count { p -> points.sumOf { p.distanceTo(it) } < 10000 }.also { println(it) }
}

private data class Point(val x: Int, val y: Int)

private fun allSurrounding(min: Point, max: Point) = sequence {
    for (x in min.x - 1..max.x + 1) {
        for (y in min.y - 1..max.y + 1) yield(Point(x, y))
    }
}

private fun Point.distanceTo(p: Point) = abs(x - p.x) + abs(y - p.y)

private fun Point.belongsTo(center: Point, otherCenters: List<Point>) = otherCenters.asSequence()
    .filter { it != center }
    .minOfOrNull { distanceTo(it) }
    ?.let { it > distanceTo(center) } ?: true