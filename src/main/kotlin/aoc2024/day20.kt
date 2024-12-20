package aoc2024

fun main() {
    data class Point(val x: Int, val y: Int)

    val grid = generateSequence(::readLine).flatMapIndexed { y, s -> s.mapIndexed { x, c -> Point(x, y) to c } }.toMap()
    fun Point.next() = sequenceOf(copy(x = x + 1), copy(y = y + 1), copy(x = x - 1), copy(y = y - 1))
        .filter { it in grid }

    data class State(val pos: Point)

    fun State.next(): List<State> = pos.next().filter { grid[it] != '#' }.map { copy(pos = it) }.toList()

    val start = State(grid.filterValues { it == 'S' }.keys.single())

    val V = mutableSetOf<State>()
    val D = mutableMapOf(start to 0)
    val U = mutableSetOf(start)
    val P = mutableMapOf<State, Set<State>>()

    do {
        val curr = U.minBy { D.getValue(it) }

        for (next in curr.next().filter { it !in V }) {
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

    println(D.filterKeys { grid[it.pos] == 'E' }.values.min())
}
