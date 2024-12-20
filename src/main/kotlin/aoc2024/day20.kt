package aoc2024

fun main() {
    data class Point(val x: Int, val y: Int)

    val grid = generateSequence(::readLine).flatMapIndexed { y, s -> s.mapIndexed { x, c -> Point(x, y) to c } }.toMap()
    fun Point.next() = sequenceOf(copy(x = x + 1), copy(y = y + 1), copy(x = x - 1), copy(y = y - 1))
        .filter { it in grid }

    fun Point.walk(): List<Point> = next().filter { grid[it] != '#' }.toList()

    val start = grid.filterValues { it == 'S' }.keys.single()

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

    val cheatSavings = grid.filter { it.key in D.keys && it.value != '#' }.keys.flatMap { s ->
        s.next().filter { grid[it] == '#' }.flatMap { o ->
            o.next().filter { it in D.keys && grid[it] != '#' && D.getValue(it) > D.getValue(s) }.map { e ->
                D.getValue(e) - D.getValue(s) - 2
            }
        }
    }.filter { it > 0 }

    println(cheatSavings.count { it >= 100 })
}
