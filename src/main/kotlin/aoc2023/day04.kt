package aoc2023

fun main() = day04(System.`in`.bufferedReader().lines().toList()).forEach(::println)

private fun day04(lines: List<String>): List<Any?> {
    var part1 = 0
    val part2Cards = MutableList(lines.size) { 1 }
    lines.forEachIndexed { game, line ->
        val numbers = line.split(" ").mapNotNull(String::toIntOrNull)
        val winningNumbers = numbers.take(10)
        val myNumbers = numbers.drop(10)
        val myWinningNumbers = myNumbers.filter { it in winningNumbers }
        var score = 0
        repeat(myWinningNumbers.size) { score = if (score == 0) 1 else score * 2 }

        repeat(myWinningNumbers.size) { i ->
            if (game + i + 1 in lines.indices)
                part2Cards[game + i + 1] += part2Cards[game]
        }

        part1 += score
    }
    return listOf(part1, part2Cards.sumOf { it })
}