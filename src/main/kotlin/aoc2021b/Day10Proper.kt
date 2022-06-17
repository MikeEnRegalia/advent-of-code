package aoc2021b

/**
 * DISCLAIMER: This is my attempt at a great solution,
 * inspired by my own previous attempt(s) and all the other solutions out there.
 * Haven't studied any solution in detail except for the notion of
 * "using a stack" and "keep a map of the tokens"
 * My original solution is in Day10.kt.
 */

fun main() {
    Day10Proper.solve(generateSequence(::readLine).toList()).forEach(::println)
}

object Day10Proper {
    fun solve(lines: List<String>) = with(lines) { listOf(part1(), part2()) }

    private fun List<String>.part1() = mapNotNull(Day10Proper::scoreError).sumOf { it }
    private fun List<String>.part2() = mapNotNull(Day10Proper::scoreAutocomplete).let { it.sorted()[it.size / 2] }

    private fun scoreError(line: String): Int? = ArrayDeque<Char>().run {
        line.forEach { c -> pushPopOrElse(c) { return c.errorScore() } }
        null
    }

    private fun scoreAutocomplete(line: String): Long? = ArrayDeque<Char>()
        .apply { line.forEach { pushPopOrElse(it) { return null } } }
        .reversed().fold(0L) { acc, c -> acc * 5 + c.acScore() }

    private inline fun ArrayDeque<Char>.pushPopOrElse(c: Char, onError: (Char) -> Nothing) {
        if (c in chunks.keys) addLast(c)
        else if (lastOrNull() == c.chunkKey()) removeLast()
        else onError(c)
    }

    private val chunks = listOf("()", "[]", "{}", "<>").associate { it[0] to it[1] }
    private val errorScores = listOf(3, 57, 1197, 25137)

    private fun Char.chunkKey() = chunks.entries.single { it.value == this }.key
    private fun Char.errorScore() = chunks.values.let { errorScores[it.indexOf(this)] }
    private fun Char.acScore() = chunks.keys.indexOf(this) + 1
}