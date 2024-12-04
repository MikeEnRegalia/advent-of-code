package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()

    fun points() = sequence {
        for (y in lines.indices) for (x in lines[y].indices) yield(x to y)
    }

    points().sumOf { (x, y) ->
        sequenceOf(
            (0..3).map { lines.getOrNull(y)?.getOrNull(x + it) },
            (0..3).map { lines.getOrNull(y + it)?.getOrNull(x) },
            (0..3).map { lines.getOrNull(y + it)?.getOrNull(x + it) },
            (0..3).map { lines.getOrNull(y + it)?.getOrNull(x - it) },
        ).map { it.joinToString("") }.count { it == "XMAS" || it == "SAMX" }
    }.also(::println)

    points().count { (x, y) ->
        sequenceOf(
            (-1..1).map { lines.getOrNull(y + it)?.getOrNull(x + it) },
            (-1..1).map { lines.getOrNull(y + it)?.getOrNull(x - it) },
        ).map { it.joinToString("") }.count { it == "MAS" || it == "SAM" } == 2
    }.also(::println)
}
