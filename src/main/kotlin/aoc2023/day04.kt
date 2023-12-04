package aoc2023

fun main() = day04(System.`in`.bufferedReader().lines().toList()).forEach(::println)

private fun day04(lines: List<String>): List<Any?> {
    var part1 = 0
    val part2Cards = MutableList(lines.size) { 1 }
    lines.forEachIndexed { game, line ->
        val (winningNumbers, myNumbers) = line.split("|")
            .map { it.split(" ").mapNotNull(String::toIntOrNull) }

        val wonCards = myNumbers.filter { it in winningNumbers }.size

        val score = (1..wonCards).fold(0) { acc, _ -> if (acc == 0) 1 else acc * 2 }

        for (i in game + 1 until game + 1 + wonCards)
            if (i in lines.indices)
                part2Cards[i] += part2Cards[game]

        part1 += score
    }
    return listOf(part1, part2Cards.sumOf { it })
}