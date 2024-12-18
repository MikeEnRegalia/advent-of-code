package aoc2024

import kotlin.math.min

fun main() {
    data class Point(val x: Int, val y: Int)

    val (X, Y) = 70 to 70
    val start = Point(0, 0)
    val end = Point(X, Y)

    val allObstacles = generateSequence(::readLine)
        .map { it.split(",").map(String::toInt).let { (x, y) -> Point(x, y) } }.toList()

    val obstacles = allObstacles.take(1024).toMutableSet()

    fun solve(): Int? {
        val U = mutableSetOf(start)
        val D = mutableMapOf(start to 0)
        val V = mutableSetOf<Point>()

        while (U.isNotEmpty()) {
            val curr = U.minBy(D::getValue)
            with(curr) { sequenceOf(copy(x = x + 1), copy(y = y + 1), copy(x = x - 1), copy(y = y - 1)) }
                .filter { it.x in 0..X && it.y in 0..Y && it !in obstacles && it !in V }
                .onEach { D[it] = min(D.getValue(curr) + 1, D[it] ?: Int.MAX_VALUE) }
                .also(U::addAll)

            if (curr == end) return D[curr]
            V += curr
            U -= curr
        }

        return null
    }

    println(solve())

    allObstacles.asSequence().drop(1024).onEach(obstacles::add).first { solve() == null }
        .also { (x, y) -> println("$x,$y") }
}
