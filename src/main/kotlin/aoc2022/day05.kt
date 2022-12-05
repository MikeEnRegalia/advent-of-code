package aoc2022

import kotlin.math.min

fun main() = day05(String(System.`in`.readAllBytes())).forEach(::println)

fun day05(input: String): List<Any?> {
    fun String.parseLine() = this
    val (stacksInput, commandsInput) = input.split("\n\n").map { it.split("\n") }

    fun compute(part2: Boolean = false) = with(loadStacks(stacksInput)) {
        commandsInput.map(String::parseLine).forEach { line ->
            val (n, from, to) = line.split(" ").mapNotNull(String::toIntOrNull)
            if (part2) {
                val newCrates = mutableListOf<Char>()
                repeat(n) {
                    newCrates += this[from - 1].removeLast()
                }
                this[to - 1] += newCrates.reversed()
            } else {
                repeat(n) {
                    this[to - 1] += this[from - 1].removeLast()
                }
            }
        }
        this
    }

    return listOf(compute(), compute(part2 = true)).map { it.joinToString("") { it.last().toString() } }
}

private fun loadStacks(stacksInput: List<String>): MutableList<MutableList<Char>> {
    val stacks = MutableList((stacksInput.last().length + 2) / 4) { mutableListOf<Char>() }
    stacksInput.take(stacksInput.size - 1).reversed().forEach { line ->
        val chunked = line.chunked(4).map(String::trim)
        chunked.forEachIndexed { i, token ->
            if (token.isNotBlank()) stacks[i] += token[1]
        }
    }
    return stacks
}

private fun <S> dijkstra(start: S, neighbors: (S) -> Iterable<S>): Map<S, Int> {
    var s = start
    val v = mutableSetOf<S>()
    val u = mutableSetOf<S>()
    val d = mutableMapOf(s to 0)
    while (true) {
        neighbors(s).filter { it !in v }.forEach { n ->
            u += n
            val distance = d.getValue(s) + 1
            d.compute(n) { _, old -> min(distance, old ?: Int.MAX_VALUE) }
        }
        v += s
        u -= s
        s = u.randomOrNull() ?: break
    }
    return d
}