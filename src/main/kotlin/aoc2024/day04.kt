package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()

    fun wordsAt(x: Int, y: Int) = sequenceOf(
        (0..3).map { lines.getOrNull(y)?.getOrNull(x + it) },
        (0..3).map { lines.getOrNull(y + it)?.getOrNull(x) },
        (0..3).map { lines.getOrNull(y + it)?.getOrNull(x + it) },
        (0..3).map { lines.getOrNull(y + it)?.getOrNull(x - it) },
    ).map { it.joinToString("") }

    var part1 = 0
    for (y in lines.indices) {
        for (x in lines[y].indices) {
            part1 += wordsAt(x, y).count { it == "XMAS" || it == "SAMX" }
        }
    }
    println(part1)

    fun xWordsAt(x: Int, y: Int) = sequenceOf(
        (-1..1).map { lines.getOrNull(y + it)?.getOrNull(x + it) },
        (-1..1).map { lines.getOrNull(y + it)?.getOrNull(x - it) },
    ).map { it.joinToString("") }

    var part2 = 0
    for (y in lines.indices) {
        for (x in lines[y].indices) {
            part2 += if (xWordsAt(x, y).count { it == "MAS" || it == "SAM" } == 2) 1 else 0
        }
    }
    println(part2)
}
