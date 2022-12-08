package aoc2022

fun main() = day00(System.`in`.reader().readLines()).forEach(::println)

private fun day00(input: List<String>): List<Any?> {
    val heights = input.map { it.map(Char::digitToInt) }

    fun linesOfSight(x: Int, y: Int) = sequenceOf(
        (0 until x).reversed().map { it to y }, (x + 1 until heights[0].size).map { it to y },
        (0 until y).reversed().map { x to it }, (y + 1 until heights.size).map { x to it }
    ).map { it.map { (x, y) -> heights[y][x] } }

    fun List<Int>.countViewable(h: Int) = if (none { it >= h }) size else takeWhile { it < h }.count() + 1

    return with(heights.flatMapIndexed { y, l -> l.mapIndexed { x, h -> Triple(x, y, h) } }) {
        listOf(
            count { (x, y, h) -> linesOfSight(x, y).any { los -> los.all { it < h } } },
            maxOf { (x, y, h) -> linesOfSight(x, y).map { it.countViewable(h) }.reduce(Int::times) }
        )
    }
}
