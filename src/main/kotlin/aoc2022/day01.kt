package aoc2022

fun main() = day01(String(System.`in`.readAllBytes())).toList().forEach(::println)

fun day01(input: String) = input.split("\n\n")
    .map { it.split("\n").map(String::toInt).sum() }
    .sortedDescending()
    .let { it.first() to it.take(3).sum() }

