package aoc2022

import java.util.*

fun main() = day01Fold(String(System.`in`.readAllBytes())).forEach(::println)

fun day01(input: String) = input.split("\n\n").map { it.lines().sumOf(String::toInt) }.collectAnswers()
private fun List<Int>.collectAnswers() = sortedDescending().run { listOf(first(), take(3).sum()) }

fun day01Fold(input: String) = input.split("\n").fold(mutableListOf(0), ::addLine).collectAnswers()

private fun addLine(acc: MutableList<Int>, line: String) = acc.apply {
    if (line.isBlank()) this += 0 else this[lastIndex] = this[lastIndex] + line.toInt()
}

fun day01SortedSet(input: String): List<Int> {
    class Food(val calories: Int)
    val top = TreeSet(compareByDescending(Food::calories))
    for (elf in input.split("\n\n")) {
        top += Food(elf.lines().sumOf(String::toInt))
        if (top.size > 3) top -= top.last()
    }
    return listOf(top.first().calories, top.sumOf(Food::calories))
}