package aoc2024

import kotlin.math.abs

fun main() {
    data class Point(val x: Int, val y: Int)

    val grid = generateSequence(::readLine).flatMapIndexed { y, s -> s.mapIndexed { x, c -> Point(x, y) to c } }.toMap()
    fun Point.next() = sequenceOf(copy(x = x + 1), copy(y = y + 1), copy(x = x - 1), copy(y = y - 1))
        .filter { it in grid }

    fun Point.step() = next().filter { grid[it] != '#' }

    val start = grid.filterValues { it == 'S' }.keys.single()
    val V = mutableSetOf<Point>()
    val D = mutableMapOf(start to 0)
    val U = mutableSetOf(start)

    while (U.isNotEmpty()) {
        val curr = U.minBy { D.getValue(it) }

        for (next in curr.step().filter { it !in V }) {
            U += next
            val (prevCost, cost) = (D[next] ?: Int.MAX_VALUE) to D.getValue(curr) + 1
            if (cost < prevCost) D[next] = cost
        }

        V += curr
        U -= curr
    }

    fun Point.distanceTo(o: Point) = abs(x - o.x) + abs(y - o.y)
    fun Point.surrounding(maxD: Int) = (-maxD..maxD).flatMap { dy ->
        (-maxD..maxD).mapNotNull { dx -> Point(x + dx, y + dy).takeIf { it != this && distanceTo(it) <= maxD } }
    }

    val path = grid.filter { it.key in D.keys && it.value != '#' }.keys
    fun savings(maxD: Int) = path.flatMap { s ->
        s.surrounding(maxD).filter(path::contains)
            .mapNotNull { e -> (D.getValue(e) - D.getValue(s) - s.distanceTo(e)).takeIf { it >= 100 } }
    }

    listOf(2, 20).forEach { println(savings(it).count()) }
}
