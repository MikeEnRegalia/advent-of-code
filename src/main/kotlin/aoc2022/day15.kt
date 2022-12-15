package aoc2022

import kotlin.math.abs

private const val MAX = 4000000
private const val PART1_Y = 2000000

fun main() {
    data class Pos(val x: Int, val y: Int)

    val data = generateSequence(::readlnOrNull).map { l ->
        l.split("""[=,:\s]""".toRegex()).mapNotNull(String::toIntOrNull).let { Pos(it[0], it[1]) to Pos(it[2], it[3]) }
    }.toList()

    infix fun Pos.dist(p: Pos) = abs(x - p.x) + abs(y - p.y)

    val knownBeacons = data.map { it.second }.toSet()

    data.filter { (s, b) -> s dist Pos(s.x, PART1_Y) <= s dist b }.fold(mutableSetOf<Int>()) { acc, (s, b) ->
        val d = s.dist(b) - s.dist(Pos(s.x, PART1_Y))
        (-d..d).asSequence().map { s.x + it }.filter { Pos(it, PART1_Y) !in knownBeacons }.forEach(acc::add)
        acc
    }.size.also(::println)

    fun Pos.pIsValid() = x in 0..MAX && y in 0..MAX
    fun Pos.beyondAllSensors() = data.none { (sensor, beacon) -> dist(sensor) <= sensor.dist(beacon) }

    for ((s, b) in data) {
        val dist = s.dist(b) + 1
        for (x in 1..dist) {
            val (dx, dy) = (dist - x).let { y -> listOf(x to y, x to -y, -x to y, -x to -y) }
                .map { (x, y) -> Pos(s.x + x, s.y + y) }
                .singleOrNull { it.pIsValid() && it.beyondAllSensors() } ?: continue
            println(dx.toLong() * MAX + dy)
            return
        }
    }
}

