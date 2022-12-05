package aoc2022

fun main() = day05(String(System.`in`.readAllBytes())).forEach(::println)

fun day05(input: String): List<Any?> {
    val (stacksInput, commandsInput) = input.split("\n\n").map { it.split("\n") }

    val size = stacksInput.last().trim().substringAfterLast(' ').toInt()
    val initialStacks = with(stacksInput.dropLast(1).asReversed()) {
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