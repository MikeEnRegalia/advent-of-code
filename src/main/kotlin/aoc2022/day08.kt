package aoc2022

fun main() {
    val M = generateSequence(::readlnOrNull).map { it.map(Char::digitToInt) }.toList()

    fun linesOfSight(x: Int, y: Int) = sequenceOf(
        sequenceOf((x - 1 downTo 0), (x + 1 until M[0].size)).map { it.map { x -> x to y } },
        sequenceOf((y - 1 downTo 0), (y + 1 until M.size)).map { it.map { y -> x to y } }
    ).flatten().map { it.map { (x, y) -> M[y][x] } }

    fun List<Int>.countVisible(h: Int) = indexOfFirst { it >= h }.let { if (it == -1) size else it + 1 }

    with(M.flatMapIndexed { y, l -> l.mapIndexed { x, h -> Triple(x, y, h) } }) {
        println(count { (x, y, h) -> linesOfSight(x, y).any { it.all { oh -> oh < h } } })
        println(maxOf { (x, y, h) -> linesOfSight(x, y).map { it.countVisible(h) }.reduce(Int::times) })
    }
}

