package y2018.day06

import kotlin.math.abs

fun main() {

    val testPoints = """1, 1
1, 6
8, 3
3, 4
5, 5
8, 9""".split("\n").asSequence().loadInput()

    compute(testPoints)
    compute(generateSequence(::readLine).loadInput())
}

private fun Sequence<String>.loadInput(): List<Point> = this
    .map { it.split(", ".toRegex()) }
    .map { Point(it[0].toInt(), it[1].toInt()) }
    .toList()

private fun compute(points: List<Point>) {
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
        sum += around(++d)
            .filter { it.belongsTo(this, otherCenters) }
            .count()
            .also {
                if (it == 0) {
                    println("$this: $sum")
                    return sum
                }
            }
    }
}

private fun Point.belongsTo(center: Point, otherCenters: List<Point>): Boolean {
    val distance = distanceTo(center)
    val minOtherDistance = otherCenters.asSequence()
        .filter { it != center }
        .minOfOrNull { distanceTo(it) }

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