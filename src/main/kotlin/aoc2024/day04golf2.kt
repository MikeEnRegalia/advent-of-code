package aoc2024

fun main() {
    val grid = generateSequence(::readLine).toList()
    fun gridAt(x: Int, y: Int) = grid.getOrNull(y)?.getOrNull(x)

    println(sequence {
        for (y in grid.indices) for (x in grid[y].indices) yield(
            sequenceOf(
                (-1..1).map { gridAt(x + it, y + it) },
                (-1..1).map { gridAt(x - it, y + it) },
            ).map { it.joinToString("") }.all { it == "MAS" || it == "SAM" })
    }.count { it })
}
