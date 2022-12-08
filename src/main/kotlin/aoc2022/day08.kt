package aoc2022

fun main() = day08(System.`in`.reader().readLines()).forEach(::println)

private fun day08(input: List<String>): List<Int> {
    val H = input.map { it.map(Char::digitToInt) }

    fun linesOfSight(x: Int, y: Int) = sequenceOf(
        listOf((x - 1 downTo 0), (x + 1 until H[0].size)).map { it.map { x -> x to y } },
        listOf((y - 1 downTo 0), (y + 1 until H.size)).map { it.map { y -> x to y } }
    ).flatten().map { it.map { (x, y) -> H[y][x] } }

    fun List<Int>.isVisible(h: Int) = all { it < h }
    fun List<Int>.countVisible(h: Int) = indexOfFirst { it >= h }.let { if (it == -1) size else it + 1 }

    return with(H.flatMapIndexed { y, l -> l.mapIndexed { x, h -> Triple(x, y, h) } }) {
        listOf(
            count { (x, y, h) -> linesOfSight(x, y).any { it.isVisible(h) } },
            maxOf { (x, y, h) -> linesOfSight(x, y).map { it.countVisible(h) }.reduce(Int::times) }
        )
    }
}
