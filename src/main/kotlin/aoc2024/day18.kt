package aoc2024

import kotlin.math.min

fun main() {
    data class Point(val x: Int, val y: Int)

    val allObstacles = generateSequence(::readLine)
        .map { it.split(",").map(String::toInt).let { (x, y) -> Point(x, y) } }.toList()

    val obstacles = allObstacles.take(1024).toMutableSet()
    val (topLeft, lowerRight) = Point(0, 0) to Point(70, 70)

    fun solve(): Int? {
        val U = mutableSetOf(topLeft)
        val D = mutableMapOf(topLeft to 0)
        val V = mutableSetOf<Point>()

        while (U.isNotEmpty()) {
            val curr = U.minBy(D::getValue)
            with(curr) { sequenceOf(copy(x = x + 1), copy(y = y + 1), copy(x = x - 1), copy(y = y - 1)) }
                .filter { it.x in 0..lowerRight.x && it.y in 0..lowerRight.y && it !in obstacles && it !in V }
                .onEach { D[it] = min(D.getValue(curr) + 1, D[it] ?: Int.MAX_VALUE) }
                .also(U::addAll)

            if (curr == lowerRight) return D[curr]
            V += curr
            U -= curr
        }

        return null
    }

    println(solve())

    allObstacles.drop(1024).asSequence().onEach(obstacles::add).first { solve() == null }
        .also { (x, y) -> println("$x,$y") }
}
