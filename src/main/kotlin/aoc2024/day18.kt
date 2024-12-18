package aoc2024

import kotlin.math.min

fun main() {
    val (X, Y) = 70 to 70
    data class Point(val x: Int, val y: Int)

    val start = Point(0, 0)
    val end = Point(X, Y)

    val allObstacles = generateSequence(::readLine).map {
        it.split(",").map(String::toInt).let { (x, y) -> Point(x, y) }
    }.toList()

    fun solve(n: Int): Int? {
        val obstacles = allObstacles.take(n).toSet()

        val U = mutableSetOf(start)
        val D = mutableMapOf(start to 0)
        val V = mutableSetOf<Point>()

        do {
            val curr = U.minBy { D.getValue(it) }
            with(curr) { sequenceOf(copy(x = x + 1), copy(y = y + 1), copy(x = x - 1), copy(y = y - 1)) }
                .filter { it.x in 0..X && it.y in 0..Y }
                .filter { it !in obstacles }
                .filter { it !in V }
                .forEach { next ->
                    U += next
                    val d = D.getValue(curr) + 1
                    val prevD = D[next] ?: Int.MAX_VALUE
                    D[next] = min(d, prevD)
                }
            if (curr == end) return D[curr]
            V += curr
            U -= curr
        } while (U.isNotEmpty())

        return null
    }

    println(solve(1024))

    println((1025..allObstacles.lastIndex).first { solve(it) == null }
        .let { allObstacles[it - 1] }.let { (x, y) -> "$x,$y" })
}
