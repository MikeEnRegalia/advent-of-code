package aoc2023

import kotlin.math.min

fun main() {
    fun List<String>.findHorizontal() = indices.filter { y ->
        val toTake = min(y, size - y)
        val above = drop(y - toTake).take(toTake)
        val below = drop(y).take(toTake).reversed()
        above == below
    }.sum()

    fun List<String>.findVertical() = this[0].indices.filter { x ->
        val toTake = min(x, this[0].length - x)
        val before = this[0].indices.drop(x - toTake).take(toTake).map { x1 -> map { it[x1] } }
        val after = this[0].indices.drop(x).take(toTake).map { x1 -> map { it[x1] } }.reversed()
        before == after
    }.sum()

    val data = String(System.`in`.readAllBytes()).split("\n\n").map { it.split("\n") }
    val h = data.sumOf { it.findHorizontal() }
    val v = data.sumOf { it.findVertical() }

    println(v + 100 * h)
}

