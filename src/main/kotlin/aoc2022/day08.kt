package aoc2022

fun main() = day00(String(System.`in`.readAllBytes())).forEach(::println)

private fun day00(input: String): List<Any?> {
    data class Pos(val x: Int, val y: Int)

    val heights = input.lines().flatMapIndexed { y, l -> l.mapIndexed { x, h -> Pos(x, y) to h.digitToInt() } }.toMap()
    val (xMax, yMax) = heights.keys.maxOf { it.x } + 1 to heights.keys.maxOf { it.y } + 1

    fun Pos.linesOfSight() = sequenceOf(
        (0 until x).reversed().map { Pos(it, y) }, (x + 1 until xMax).map { Pos(it, y) },
        (0 until y).reversed().map { Pos(x, it) }, (y + 1 until yMax).map { Pos(x, it) }
    ).map { it.map(heights::getValue) }

    fun List<Int>.countViewable(fromHeight: Int) =
        if (none { it >= fromHeight }) size else takeWhile { it < fromHeight }.count() + 1

    return listOf(
        heights.count { (pos, height) -> pos.linesOfSight().any { los -> los.all { it < height } } },
        heights.maxOf { (pos, height) -> pos.linesOfSight().map { it.countViewable(height) }.reduce(Int::times) }
    )
}
