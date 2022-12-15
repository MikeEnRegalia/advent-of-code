package aoc2022

import kotlin.math.abs

fun main() {
    val data = generateSequence(::readlnOrNull).map { l ->
        l.split("""[=,:\s]""".toRegex()).mapNotNull(String::toIntOrNull).let { listOf(it[0] to it[1], it[2] to it[3]) }
    }.toList()

    fun dist(a: Pair<Int, Int>, b: Pair<Int, Int>) = abs(a.first - b.first) + abs(a.second - b.second)

    data.fold(mutableSetOf<Pair<Int, Int>>()) { acc, (s, b) ->
        val d = dist(s, b)
        val start = s.first to 2000000
        var p = start

        while (dist(s, p) < d) {
            acc += p
            p = p.first + 1 to p.second
        }
        p = start
        while (dist(s, p) <= d) {
            acc += p
            p = p.first - 1 to p.second
        }
        acc
    }.size.also(::println)

    fun Pair<Int, Int>.pIsValid() = first in 0..4000000 && second in 0..4000000
    fun Pair<Int, Int>.beyondAllSensors() = data.none { (sensor, beacon) -> dist(sensor, this) <= dist(sensor, beacon) }

    for ((s, b) in data) {
        val dist = dist(s, b) + 1
        for (x in 1..dist) {
            val y = dist - x
            val (dx, dy) = listOf(x to y, x to -y, -x to y, -x to -y)
                .map { (x, y) -> s.first + x to s.second + y }
                .singleOrNull { it.pIsValid() && it.beyondAllSensors() } ?: continue
            println(dx.toLong() * 4000000 + dy)
            return
        }
    }
}

