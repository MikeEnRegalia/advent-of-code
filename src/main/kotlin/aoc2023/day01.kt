package aoc2023

fun main() = day01(String(System.`in`.readAllBytes()).split("\n")).forEach(::println)

private fun day01(lines: List<String>): List<Any?> {
    fun String.part1() = mapNotNull(Char::digitToIntOrNull)
    fun String.part2() = with(digits) {
        listOf(keys.minBy { indexOfOrMax(it) }, keys.maxBy { lastIndexOf(it) }).map { getValue(it) }
    }

    fun List<String>.sumOfCalibrationValues(toDigits: (String) -> List<Int>) =
        sumOf { line -> toDigits(line).let { it.first() * 10 + it.last() } }

    return listOf(String::part1, String::part2).map { lines.sumOfCalibrationValues(it) }
}

private val digits = sequenceOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    .flatMapIndexed { i, s -> (i + 1).let { sequenceOf(s to it, it.toString() to it) } }
    .toMap()

fun String.indexOfOrMax(s: String) = indexOf(s).takeIf { it >= 0 } ?: Int.MAX_VALUE