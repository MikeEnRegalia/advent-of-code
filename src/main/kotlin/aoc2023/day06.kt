package aoc2023

import kotlin.math.max
import kotlin.math.min

fun main() = day06(String(System.`in`.readAllBytes())).forEach(::println)

private fun day06(input: String): List<Any?> {
    val races = input.split("\n").map { it.split(" ").mapNotNull(String::toIntOrNull) }.let { lines ->
        lines.first().indices.map { i -> lines[0][i] to lines[1][i]}
    }

    val part1 = races.map { (duration, distance) ->
        (0..duration).count { d ->
            val speed = d
            val dist = speed * (duration - d)
            dist > distance
        }
    }.map(Int::toLong).reduce(Long::times)

    return listOf(part1)
}