package aoc2022

fun main() = day02(String(System.`in`.readAllBytes())).forEach(::println)

fun day02(input: String) = with(input.lines()) { listOf(sumOf(::part1Round), sumOf(::part2Round)) }

private fun part1Round(line: String) = when (line) {
    "A X" -> 1 + 3
    "B X" -> 1 + 0
    "C X" -> 1 + 6
    "A Y" -> 2 + 6
    "B Y" -> 2 + 3
    "C Y" -> 2 + 0
    "A Z" -> 3 + 0
    "B Z" -> 3 + 6
    "C Z" -> 3 + 3
    else -> throw IllegalArgumentException(line)
}

private fun part2Round(line: String) = when (line) {
    "A X" -> 3 + 0
    "B X" -> 1 + 0
    "C X" -> 2 + 0
    "A Y" -> 1 + 3
    "B Y" -> 2 + 3
    "C Y" -> 3 + 3
    "A Z" -> 2 + 6
    "B Z" -> 3 + 6
    "C Z" -> 1 + 6
    else -> throw IllegalArgumentException(line)
}
