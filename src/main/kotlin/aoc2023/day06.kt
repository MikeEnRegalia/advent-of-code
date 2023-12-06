package aoc2023

fun main() = day06(generateSequence(::readlnOrNull).toList()).forEach(::println)

private fun day06(lines: List<String>): List<Any?> {
    data class Race(val duration: Long, val distance: Long)

    val races = lines
        .map { it.split(" ").mapNotNull(String::toLongOrNull) }
        .let { (durations, distances) -> durations.indices.map { i -> Race(durations[i], distances[i]) } }

    fun Race.wonIfWoundUpFor(woundUpFor: Long) = woundUpFor * (duration - woundUpFor) > distance

    fun List<Race>.sumWinningWays() = map { (0L..it.duration).count(it::wonIfWoundUpFor) }.reduce(Int::times)

    val part2Race = lines.map { it.filter(Char::isDigit).toLong() }.let { Race(it[0], it[1]) }

    return listOf(races, listOf(part2Race)).map { it.sumWinningWays() }
}