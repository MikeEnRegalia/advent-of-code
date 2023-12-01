package aoc2023

fun main() = day01(String(System.`in`.readAllBytes()).split("\n")).forEach(::println)

private fun day01(lines: List<String>): List<Any?> {

    val part1 = lines.sumOf { line ->
        line.mapNotNull(Char::digitToIntOrNull).let { it.first() * 10 + it.last() }
    }

    val digits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        .flatMapIndexed { i, s -> (i + 1).let { listOf(s to it, it.toString() to it) } }
        .toMap()

    val part2 = lines.sumOf { line ->
        val first = digits.getValue(digits.keys.minBy { line.indexOfOrNull(it) ?: Int.MAX_VALUE })
        val last = digits.getValue(digits.keys.maxBy { line.lastIndexOf(it) })
        first * 10 + last
    }

    return listOf(part1, part2)
}

fun String.indexOfOrNull(s: String) = indexOf(s).takeIf { it >= 0 }