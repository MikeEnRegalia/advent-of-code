package aoc2022

fun main() = day05(String(System.`in`.readAllBytes())).forEach(::println)

fun day05(input: String): List<Any?> {
    val (stacksInput, commandsInput) = input.split("\n\n").map { it.split("\n") }

    val size = stacksInput.last().split(" ").mapNotNull(String::toIntOrNull).last()
    val stacksContents = with(stacksInput) { take(size - 1).reversed() }
    val initialStacks = List(size) { stack ->
        buildList {
            for (line in stacksContents) line.getOrNull(1 + (stack * 4))?.takeIf { it != ' ' }?.let { add(it) }
        }
    }

    val commands = commandsInput.map {
        it.split(" ").mapNotNull(String::toIntOrNull).mapIndexed { i, x -> if (i == 0) x else x - 1 }
    }

    fun compute(mod: ArrayDeque<Char>.(Char) -> Unit): String {
        val stacks = List(initialStacks.size) { initialStacks[it].toMutableList() }
        for ((n, from, to) in commands)
            stacks[to] += ArrayDeque<Char>().apply { repeat(n) { mod(stacks[from].removeLast()) } }
        return stacks.joinToString("") { it.last().toString() }
    }

    return listOf(compute { addLast(it) }, compute { addFirst(it) })
}