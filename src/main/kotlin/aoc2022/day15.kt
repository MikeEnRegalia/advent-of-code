package aoc2022

import kotlin.math.abs

private const val MAX_Y = 4000000
private const val PART1_Y = 2000000

fun main() {
    val data = generateSequence(::readlnOrNull).map { l ->
        l.split("""[=,:\s]""".toRegex()).mapNotNull(String::toIntOrNull).let { listOf(it[0] to it[1], it[2] to it[3]) }
    }.toList()

    fun dist(a: Pair<Int, Int>, b: Pair<Int, Int>) = abs(a.first - b.first) + abs(a.second - b.second)

    val knownBeacons = data.map { it[1] }.toSet()

    data.filter { (s, b) -> dist(s, s.first to PART1_Y) <= dist(s, b) }.fold(mutableSetOf<Int>()) { acc, (s, b) ->
        val d = dist(s, b) - dist(s, s.first to PART1_Y)
        (-d..d).asSequence().map { s.first + it }.filter { it to PART1_Y !in knownBeacons }.forEach(acc::add)
        acc
    }.size.also(::println)

    fun Pair<Int, Int>.pIsValid() = first in 0..MAX_Y && second in 0..MAX_Y
    fun Pair<Int, Int>.beyondAllSensors() = data.none { (sensor, beacon) -> dist(sensor, this) <= dist(sensor, beacon) }

    for ((s, b) in data) {
        val dist = dist(s, b) + 1
        for (x in 1..dist) {
            val y = dist - x
            val (dx, dy) = listOf(x to y, x to -y, -x to y, -x to -y)
                .map { (x, y) -> s.first + x to s.second + y }
                .singleOrNull { it.pIsValid() && it.beyondAllSensors() } ?: continue
            println(dx.toLong() * MAX_Y + dy)
            return
        }
    }
}

