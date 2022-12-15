package aoc2022

import kotlin.math.abs

private const val MAX = 4000000
private const val PART1_Y = 2000000

fun main() {
    data class Pos(val x: Int, val y: Int) {
        infix fun dist(p: Pos) = abs(x - p.x) + abs(y - p.y)
        fun isValid() = x in 0..MAX && y in 0..MAX
    }

    data class Sensor(val pos: Pos, val detectedBeacon: Pos) {
        val beaconDistance = pos dist detectedBeacon
    }

    val sensors = generateSequence(::readlnOrNull).map { l ->
        l.split("""[=,:\s]""".toRegex()).mapNotNull(String::toIntOrNull)
            .let { Sensor(Pos(it[0], it[1]), Pos(it[2], it[3])) }
    }.toList()

    val knownBeacons = buildSet { sensors.forEach { add(it.detectedBeacon) } }

    sensors.filter { (s, b) -> s dist Pos(s.x, PART1_Y) <= s dist b }.fold(mutableSetOf<Int>()) { acc, (s, b) ->
        val d = s.dist(b) - s.dist(Pos(s.x, PART1_Y))
        (-d..d).asSequence().map { s.x + it }.filter { Pos(it, PART1_Y) !in knownBeacons }.forEach(acc::add)
        acc
    }.size.also(::println)

    fun Pos.isOutOfRange() = sensors.none { (sensor, beacon) -> dist(sensor) <= sensor.dist(beacon) }

    for ((s, b) in sensors.sortedBy { it.beaconDistance }) {
        val dist = s.dist(b) + 1
        for (x in dist downTo 1) {
            val (dx, dy) = (dist - x).let { y -> sequenceOf(x to y, x to -y, -x to y, -x to -y) }
                .map { (x, y) -> Pos(s.x + x, s.y + y) }
                .singleOrNull { it.isValid() && it.isOutOfRange() } ?: continue
            println(dx.toLong() * MAX + dy)
            return
        }
    }
}

