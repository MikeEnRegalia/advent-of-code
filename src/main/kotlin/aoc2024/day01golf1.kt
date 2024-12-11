package aoc2024

import kotlin.math.abs

fun main() {
    val data = generateSequence(::readLine).map { it.split("   ").map(String::toInt) }.toList()
    fun extractAndSort(i: Int) = data.map { it[i] }.sorted()
    println(extractAndSort(0).zip(extractAndSort(1)).sumOf { abs(it.first - it.second) })
}

