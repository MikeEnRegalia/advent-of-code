package aoc2022

import kotlin.math.abs

private const val MAX = 4000000
private const val PART1_Y = 2000000

fun main() {
    data class Pos(val x: Int, val y: Int) {
        fun dist(p: Pos) = abs(x - p.x) + abs(y - p.y)
        fun isValid() = x in 0..MAX && y in 0..MAX
        operator fun plus(pos: Pos) = Pos(x + pos.x, y + pos.y)
        fun all(x: Int, y: Int) = sequenceOf(Pos(x, y), Pos(x, -y), Pos(-x, y), Pos(-x, -y)).map { this + it }
    }

    data class Sensor(val pos: Pos, val beacon: Pos) {
        val beaconDistance = pos.dist(beacon)
    }

    val sensors = generateSequence(::readlnOrNull).mapTo(mutableListOf()) { l ->
        l.split(Regex("""[=,:\s]""")).mapNotNull(String::toIntOrNull)
            .let { Sensor(Pos(it[0], it[1]), Pos(it[2], it[3])) }
    } as List<Sensor>

    val knownBeacons = buildSet { sensors.forEach { add(it.beacon) } }

    sensors.filter { abs(it.pos.y - PART1_Y) < it.beaconDistance }.fold(mutableSetOf<Int>()) { acc, sensor ->
        val d = sensor.beaconDistance - sensor.pos.dist(Pos(sensor.pos.x, PART1_Y))
        (-d..d).asSequence().map { sensor.pos.x + it }.filter { Pos(it, PART1_Y) !in knownBeacons }
            .forEach(acc::add)
        acc
    }.size.also(::println)

    fun Pos.isOutOfRange() = sensors.none { (sensor, beacon) -> dist(sensor) <= sensor.dist(beacon) }

    for (sensor in sensors.sortedBy { it.beaconDistance }) {
        for (x in sensor.beaconDistance + 1 downTo 1) for (p in sensor.pos.all(x, y = sensor.beaconDistance + 1 - x)) {
            if (p.isValid() && p.isOutOfRange()) {
                println(p.x.toLong() * MAX + p.y)
                return
            }
        }
    }
}

