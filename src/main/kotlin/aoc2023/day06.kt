package aoc2023

import util.remove

fun main() = day06(generateSequence(::readlnOrNull).toList()).forEach(::println)

private fun day06(lines: List<String>): List<Any?> {
    data class Race(val duration: Long, val distance: Long)

    val races = lines
        .map { it.split(" ").mapNotNull(String::toLongOrNull) }
        .let { lines -> lines.first().indices.map { i -> Race(lines[0][i], lines[1][i]) } }

    fun Race.wonIfWoundUpFor(woundUpFor: Long) = woundUpFor * (duration - woundUpFor) > distance

    fun List<Race>.solve() = map { race ->
        (0L..race.duration).count { race.wonIfWoundUpFor(it) }
    }.reduce(Int::times)

    val part2Race = lines.map { it.filter(Char::isDigit).toLong() }.let { Race(it[0], it[1]) }

    return listOf(races, listOf(part2Race)).map { it.solve() }
}