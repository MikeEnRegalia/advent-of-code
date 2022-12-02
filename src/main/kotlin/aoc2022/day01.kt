package aoc2022

import java.io.File

fun main() = day01Fold(String(System.`in`.readAllBytes())).forEach(::println)

fun day01(input: String) = input.split("\n\n").map { it.lines().sumOf(String::toInt) }.collectAnswers()
private fun List<Int>.collectAnswers() = sortedDescending().run { listOf(first(), take(3).sum()) }

fun day01Fold(input: String) = input.split("\n").fold(mutableListOf(0), ::addLine).collectAnswers()

private fun addLine(acc: MutableList<Int>, line: String) = acc.apply {
    if (line.isBlank()) this += 0 else this[lastIndex] = this[lastIndex] + line.toInt()
}

fun day01MinMax(input: String): List<Int> {
    val top = mutableListOf<Int>()
    for (elf in input.split("\n\n")) {
        val elfCalories = elf.lines().sumOf(String::toInt)
        val min = top.min()
        if (elfCalories > min) {
            top += elfCalories
            if (top.size > 3) top -= min
        }
    }
    return listOf(top.max(), top.sum())
}
