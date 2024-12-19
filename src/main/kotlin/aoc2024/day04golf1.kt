package aoc2024

fun main() {
    val grid = generateSequence(::readLine).toList()
    val four = 0..3
    fun gridAt(x: Int, y: Int) = grid.getOrNull(y)?.getOrNull(x)

    println(sequence {
        for (y in grid.indices) for (x in grid[y].indices) yield(
            sequenceOf(
                four.map { gridAt(x + it, y) },
                four.map { gridAt(x, y + it) },
                four.map { gridAt(x + it, y + it) },
                four.map { gridAt(x - it, y + it) },
            ).map { it.joinToString("") }.count { it == "XMAS" || it == "SAMX" }
        )
    }.sum())
}
