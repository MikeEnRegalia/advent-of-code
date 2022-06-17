package aoc2021b

import kotlin.math.abs

fun main() = generateSequence(::readLine).single().split(",").map { it.toInt() }.day07().forEach(::println)

fun List<Int>.day07() = listOf(minFuel(), minFuel(Int::sumFromZero))

private fun List<Int>.minFuel(spent: (Int) -> Int = { it }) = (0..maxOf { it }).minOf { p -> sumOf { spent(abs(p - it)) } }

private fun Int.sumFromZero() = this * (this + 1) / 2