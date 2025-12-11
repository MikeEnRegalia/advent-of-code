package aoc2025

import kotlin.math.sqrt

fun main() {
    data class Point(val x: Long, val y: Long, val z: Long)

    fun Point.distanceTo(o: Point): Double =
        sqrt(sequenceOf(o.x - x, o.y - y, o.z - z).sumOf { it * it }.toDouble())

    val points = generateSequence(::readLine).map { line ->
        line.split(",").map { it.toLong() }.let { Point(it[0], it[1], it[2]) }
    }.toSet()

    val connections = points.flatMap { a -> points.filter { it != a }.map { b -> setOf(a, b) } }
        .distinct().sortedBy { it.first().distanceTo(it.last()) }

    val circuits = points.map { mutableSetOf(it) }.toMutableSet()

    for ((n, c) in connections.withIndex()) {
        if (n == 1000) {
            println(circuits.map { it.size }.sortedDescending().take(3).reduce { a, b -> a * b })
        }

        val (a, b) = c.first() to c.last()

        val ca = circuits.single { a in it }
        val cb = circuits.single { b in it }

        if (ca != cb) {
            ca.addAll(cb)
            circuits.removeIf { it == cb }
        }
        if (circuits.size == 1) {
            println(a.x * b.x)
            break
        }
    }

}
