package aoc2022

fun main() = day08(System.`in`.reader().readLines()).forEach(::println)

private fun day08(input: List<String>): List<Int> {
    val H = input.map { it.map(Char::digitToInt) }

    fun linesOfSight(x: Int, y: Int) = sequenceOf(
        (0 until x).reversed().map { it to y }, (x + 1 until H[0].size).map { it to y },
        (0 until y).reversed().map { x to it }, (y + 1 until H.size).map { x to it }
    ).map { it.map { (x, y) -> H[y][x] } }

    fun List<Int>.isVisible(h: Int) = all { it < h }
    fun List<Int>.countVisible(h: Int) = indexOfFirst { it >= h }.let { if (it == -1) size else it + 1 }

    return with(H.flatMapIndexed { y, l -> l.mapIndexed { x, h -> Triple(x, y, h) } }) {
        listOf(
            count { (x, y, h) -> linesOfSight(x, y).any { it.isVisible(h) } },
            maxOf { (x, y, h) -> linesOfSight(x, y).map { it.countVisible(h) }.reduce(Int::times) }
        )
    }
}
