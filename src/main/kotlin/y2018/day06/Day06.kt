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

    println("min: $min, max: $max")

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

private fun Point.belongsTo(center: Point, otherCenters: List<Point>) = otherCenters.asSequence()
    .filter { it != center }
    .minOfOrNull { distanceTo(it) }
    ?.let { it > distanceTo(center) } ?: true

data class Point(val x: Int, val y: Int) {
    override fun toString() = "${x}x$y"
    fun around(d: Int) = sequence {
        for (dx in -d..d) {
            for (dy in -d..d) {
                if (dx == -d || dx == d || dy == -d || dy == d)
                    yield(Point(x + dx, y + dy))
            }
        }
    }

    fun distanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)
}