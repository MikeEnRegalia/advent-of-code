package aoc2022

fun main() = day06(String(System.`in`.readAllBytes())).forEach(::println)

private fun day06(s: String) = listOf(4, 14).map { n -> s.windowed(n).indexOfFirst { it.toSet().size == n } + n }
