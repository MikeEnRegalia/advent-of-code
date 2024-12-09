package aoc2024

fun main() {
    val grid = generateSequence(::readLine).toList()
    fun gridAt(x: Int, y: Int) = grid.getOrNull(y)?.getOrNull(x)
    fun points() = sequence { for (y in grid.indices) for (x in grid[y].indices) yield(x to y) }

    points().sumOf { (x, y) ->
        sequenceOf(
            (0..3).map { gridAt(x + it, y) },
            (0..3).map { gridAt(x, y + it) },
            (0..3).map { gridAt(x + it, y + it) },
            (0..3).map { gridAt(x - it, y + it) },
        ).map { it.joinToString("") }.count { it == "XMAS" || it == "SAMX" }
    }.also(::println)

    points().count { (x, y) ->
        sequenceOf(
            (-1..1).map { gridAt(x + it, y + it) },
            (-1..1).map { gridAt(x - it, y + it) },
        ).map { it.joinToString("") }.all { it == "MAS" || it == "SAM" }
    }.also(::println)
}
