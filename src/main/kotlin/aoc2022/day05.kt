package aoc2022

fun main() = day05(String(System.`in`.readAllBytes())).forEach(::println)

fun day05(input: String): List<Any?> {
    val (stacksInput, commandsInput) = input.split("\n\n").map { it.split("\n") }

    val size = stacksInput.last().split(" ").mapNotNull(String::toIntOrNull).last()
    val stacksContents = with(stacksInput) { take(size - 1).reversed() }
    val initialStacks = List(size) { stack ->
        buildList {
            for (line in stacksContents) {
                line.getOrNull(1 + (stack * 4))?.takeIf { it != ' ' }?.let { add(it) }
            }
        }
    }

    fun compute(mod: List<Char>.() -> List<Char> = { this }) =
        List(initialStacks.size) { initialStacks[it].toMutableList() }.let { stacks ->
            for (line in commandsInput) {
                val (n, from, to) = line.split(" ").mapNotNull(String::toIntOrNull)
                    .mapIndexed { i, x -> if (i == 0) x else x - 1 }
                stacks[to] += buildList { repeat(n) { add(stacks[from].removeLast()) } }.mod()
            }
            stacks.joinToString("") { it.last().toString() }
        }

    return listOf(compute(), compute { reversed() })
}