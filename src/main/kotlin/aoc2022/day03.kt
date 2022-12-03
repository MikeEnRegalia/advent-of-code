package aoc2022

fun main() = day03(String(System.`in`.readAllBytes())).forEach(::println)

fun day03(input: String): List<Any?> {
    fun Char.score() = when (this) {
        in 'a'..'z' -> this - 'a' + 1
        else -> this - 'A' + 27
    }

    val part1 = input.lines().sumOf { line ->
        val (a, b) = line.chunked(line.length / 2)
        line.toSet().filter { it in a && it in b }.sumOf(Char::score)
    }

    val part2 = input.lines().chunked(3).sumOf { group ->
        val inAll = group.flatMap { it.toList() }.toSet().filter { c -> group.all { c in it } }
        inAll.single().score()
    }
    return listOf(part1, part2)
}