package aoc2022

import kotlin.math.abs

fun main() = day15(String(System.`in`.readAllBytes())).forEach(::println)

private fun day15(input: String): List<Any?> {
    fun dist(a: Pair<Int, Int>, b: Pair<Int, Int>) = abs(a.first - b.first) + abs(a.second - b.second)
    val data = input.lines().map {
        it.map { if (it == '-' || it.isDigit()) it else ' ' }.joinToString("").split(" ")
            .mapNotNull(String::toIntOrNull)
    }
    val noBeacons = data.fold(mutableSetOf<Pair<Int, Int>>()) { acc, (sx, sy, bx, by) ->
        val sensor = sx to sy
        val beacon = bx to by
        val safe = dist(sensor, beacon)
        val start = sx to 2000000
        var s = start
        while (dist(sensor, s) < safe) {
            acc += s
            s = s.first + 1 to s.second
        }
        s = start
        while (dist(sensor, s) <= safe) {
            acc += s
            s = s.first - 1 to s.second
        }
        acc
    }.size

    fun Pair<Int, Int>.pIsValid() = first in 0..4000000 && second in 0..4000000
    fun Pair<Int, Int>.undetected() = data.none { (sx, sy, bx, by) -> dist(sx to sy, this) <= dist(sx to sy, bx to by)}

    val beacon = data.fold(mutableSetOf<Pair<Int, Int>>()) { acc, (sx, sy, bx, by) ->
        val sensor = sx to sy
        val dist = dist(sensor, bx to by) + 1
        val sensorPoints = buildSet {
            for (x in 1..dist) {
                val y = dist - x
                listOf(x to y, x to -y, -x to y, -x to -y)
                    .map { (x, y) -> sx + x to sy + y }
                    .onEach { assert(dist(sensor, it) == dist) }
                    .filter { it.pIsValid() }
                    .forEach {
                        if (it.undetected()) add(it)
                    }
            }
        }
        acc += sensorPoints
        acc
    }.single()


    return listOf(noBeacons, beacon.first.toBigInteger() * 4000000.toBigInteger() + beacon.second.toBigInteger())
}

