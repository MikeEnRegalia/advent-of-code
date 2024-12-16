package aoc2024

fun main() {
    data class Point(val x: Int, val y: Int)

    fun Point.move(facing: Char) = when (facing) {
        'N', 'S' -> copy(y = y + if (facing == 'N') -1 else 1)
        'W', 'E' -> copy(x = x + if (facing == 'W') -1 else 1)
        else -> throw IllegalArgumentException(facing.toString())
    }

    val grid = generateSequence(::readLine).flatMapIndexed { y, s -> s.mapIndexed { x, c -> Point(x, y) to c } }.toMap()

    data class State(val pos: Point, val facing: Char) {
        fun next() = sequenceOf(forward(), turn(3), turn(1)).filterNotNull()
        fun forward() = copy(pos = pos.move(facing)).takeIf { s -> grid[s.pos].let { it != null && it in ".E" } }
        fun turn(d: Int) = copy(facing = "NESW".let { it[(it.indexOf(facing) + d) % it.length] })
    }

    val start = State(grid.filterValues { it == 'S' }.keys.single(), 'E')

    val V = mutableSetOf<State>()
    val D = mutableMapOf(start to 0)
    val U = mutableSetOf(start)
    val P = mutableMapOf<State, Set<State>>()

    do {
        val curr = U.minBy { D.getValue(it) }

        for (next in curr.next().filter { it !in V }) {
            U += next
            val (prevCost, cost) = D[next] to D.getValue(curr) + if (curr.facing == next.facing) 1 else 1000
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

    println(D.filterKeys { grid[it.pos] == 'E' }.minOf { it.value })

    with(P.filterKeys { grid[it.pos] == 'E' }.keys.toMutableSet()) {
        while (true) if (mapNotNull { P[it] }.none(::addAll)) break
        println(distinctBy { it.pos }.size)
    }
}
