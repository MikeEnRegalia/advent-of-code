package aoc2022

fun main() = day01(String(System.`in`.readAllBytes())).forEach(::println)

fun day01(input: String) = input.split("\n\n")
    .map { it.split("\n").sumOf(String::toInt) }
    .sortedDescending()
    .let { listOf(it.first(), it.take(3).sum()) }
