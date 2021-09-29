package y2018.day06

import kotlin.math.abs

fun main() {
    val points = generateSequence(::readLine)
        .map { it.split(", ".toRegex()) }
        .map { Point(it[0].toInt(), it[1].toInt()) }
        .toList()

    val (min, max) = with(points) {
        Point(minOf { it.x }, minOf { it.y }) to Point(maxOf { it.x }, maxOf { it.y })
    }

    points
        .filter { it.x in (min.x + 1 until max.x) }
        .filter { it.y in (min.y + 1 until max.y) }
        .maxOf { it.area(points) }
        .also { println(it) }
}

private fun Point.area(otherCenters: List<Point>): Int {
    var d = 0
    var sum = 1
    while (true) {
        sum += around(d++)
            .filter { it.belongsTo(this, otherCenters) }
            .count()
            .also {
                if (it == 0) return sum
                println("$this: $d: $it")
            }
    }
}

private fun Point.belongsTo(center: Point, otherCenters: List<Point>): Boolean {
    val distance = distanceTo(center)
    val minOtherDistance = otherCenters
        .filter { it != center }
        .map { it to distanceTo(it) }
        .minOfOrNull { it.second }

    return minOtherDistance == null || minOtherDistance > distance
}

data class Point(val x: Int, val y: Int) {
    override fun toString() = "${x}x$y"
    fun around(d: Int) = sequence {
        for (x1 in (x - d)..(x + d)) {
            for (y1 in (y - d)..(y + d)) {
                if (x1 == x - d || x1 == x + d || y1 == y - d || y1 == y + d)
                    yield(Point(x1, y1))
            }
        }
    }

    fun distanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)
}