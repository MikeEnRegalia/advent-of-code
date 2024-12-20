package aoc2024

import kotlin.math.abs

fun main() {
    data class Point(val x: Int, val y: Int)
    fun Point.distanceTo(o: Point) = abs(x - o.x) + abs(y - o.y)

    val grid = generateSequence(::readLine).flatMapIndexed { y, s -> s.mapIndexed { x, c -> Point(x, y) to c } }.toMap()
    fun Point.next() = sequenceOf(copy(x = x + 1), copy(y = y + 1), copy(x = x - 1), copy(y = y - 1))
        .filter { it in grid }

    fun solve(start: Point): Map<Point, Int> {
        fun Point.walk(): List<Point> = next().filter { grid[it] != '#' }.toList()

        val V = mutableSetOf<Point>()
        val D = mutableMapOf(start to 0)
        val U = mutableSetOf(start)
        val P = mutableMapOf<Point, Set<Point>>()

        do {
            val curr = U.minBy { D.getValue(it) }

            for (next in curr.walk().filter { it !in V }) {
                U += next
                val (prevCost, cost) = D[next] to D.getValue(curr) + 1
                when {
                    prevCost == null || cost < prevCost -> {
                        D[next] = cost
                        P[next] = setOf(curr)
                    }

                    cost == prevCost -> P[next] = P.getValue(next) + curr
                }
            }

            V += curr
            U -= curr
        } while (U.isNotEmpty())
        return D
    }


    fun Point.surrounding(maxD: Int) = (-maxD..maxD).flatMap { dy ->
        (-maxD..maxD).mapNotNull { dx -> Point(x + dx, y + dy).takeIf { it != this && distanceTo(it) <= maxD } }
    }

    val D = solve(grid.filterValues { it == 'S' }.keys.single())
    val path = grid.filter { it.key in D.keys && it.value != '#' }.keys

    fun savings(maxD: Int, min: Int) = path.flatMap { s ->
        s.surrounding(maxD).filter(path::contains).mapNotNull { e ->
            (D.getValue(e) - D.getValue(s) - s.distanceTo(e)).takeIf { it >= min }
        }
    }

    println(savings(2, 100).count())
    println(savings(20, 100).count())
}
