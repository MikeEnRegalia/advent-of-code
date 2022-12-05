package aoc2022

fun main() = day05B(String(System.`in`.readAllBytes())).forEach(::println)

// inspired by other solutions
fun day05B(input: String): List<Any?> {
    fun solve(part2: Boolean) = with(mutableMapOf<Int, ArrayDeque<Char>>()) {
        for (l in input.lines()) when {
            l.contains("[") -> l.chunked(4).map { it[1] }.forEachIndexed { i, c ->
                if (c != ' ') compute(i + 1) { _, old -> (old ?: ArrayDeque()).apply { addFirst(c) } }
            }

            l.startsWith("move") -> l.split(" ").mapNotNull(String::toIntOrNull).let { (n, from, to) ->
                getValue(to) += List(n) { getValue(from).removeLast() }.run { if (part2) asReversed() else this }
            }
        }
        entries.sortedBy { it.key }.joinToString("") { it.value.last().toString() }
    }
    return listOf(solve(part2 = false), solve(part2 = true))
}

// my own refactored solution
fun day05(input: String): List<Any?> {
    val (cratesInput, commandsInput) = input.split("\n\n").map { it.split("\n") }

    val size = cratesInput.last().trim().substringAfterLast(' ').toInt()
    val initialStacks = with(cratesInput.dropLast(1).asReversed()) {
        List(size) { i -> mapNotNull { it.getOrNull(1 + (i * 4))?.takeUnless(Char::isWhitespace) } }
    }

    val commands = commandsInput.map {
        it.split(" ").mapNotNull(String::toIntOrNull).mapIndexed { i, x -> if (i == 0) x else x - 1 }
    }

    fun lift(dropCrates: (List<Char>) -> List<Char> = { it }): String {
        val stacks = initialStacks.map { it.toMutableList() }
        for ((n, from, to) in commands) stacks[to].addAll(dropCrates(stacks[from].removeLast(n)))
        return stacks.joinToString("") { it.last().toString() }
    }

    return listOf(lift { it.asReversed() }, lift())
}

private fun <T> MutableList<T>.removeLast(n: Int) =
    buildList { repeat(n) { add(this@removeLast.removeLast()) } }.asReversed()