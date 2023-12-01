package aoc2023

fun main() = day01(String(System.`in`.readAllBytes()).split("\n")).forEach(::println)

private fun day01(lines: List<String>): List<Any?> {
    fun String.part1() = mapNotNull(Char::digitToIntOrNull)
    fun String.part2() = listOf(
        digits.getValue(digits.keys.minBy { indexOfOrNull(it) ?: Int.MAX_VALUE }),
        digits.getValue(digits.keys.maxBy { lastIndexOf(it) })
    )

    fun List<Int>.firstLastDigits() = first() * 10 + last()

    return listOf(
        lines.sumOf { it.part1().firstLastDigits() },
        lines.sumOf { it.part2().firstLastDigits() }
    )
}

private val digits = sequenceOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    .flatMapIndexed { i, s -> (i + 1).let { sequenceOf(s to it, it.toString() to it) } }
    .toMap()

fun String.indexOfOrNull(s: String) = indexOf(s).takeIf { it >= 0 }