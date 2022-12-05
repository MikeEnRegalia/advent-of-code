package aoc2022

fun main() = day05(String(System.`in`.readAllBytes())).forEach(::println)

fun day05(input: String): List<Any?> {
    val (stacksInput, commandsInput) = input.split("\n\n").map { it.split("\n") }

    fun loadStacks(stacksInput: List<String>): MutableList<MutableList<Char>> {
        val stacks = MutableList((stacksInput.last().length + 2) / 4) { mutableListOf<Char>() }
        stacksInput.take(stacksInput.size - 1).reversed().forEach { line ->
            val chunked = line.chunked(4).map(String::trim)
            chunked.forEachIndexed { i, token ->
                if (token.isNotBlank()) stacks[i] += token[1]
            }
        }
        return stacks
    }

    fun compute(part1: Boolean = false) = loadStacks(stacksInput).also { stacks ->
        for (line in commandsInput) {
            val (n, from, to) = line.split(" ").mapNotNull(String::toIntOrNull)
            if (part1) {
                repeat(n) { stacks[to - 1] += stacks[from - 1].removeLast() }
            } else {
                val newCrates = mutableListOf<Char>()
                repeat(n) { newCrates += stacks[from - 1].removeLast() }
                stacks[to - 1] += newCrates.reversed()
            }
        }
    }

    return listOf(compute(part1 = true), compute(part1 = false))
        .map { it.joinToString("") { it.last().toString() } }
}