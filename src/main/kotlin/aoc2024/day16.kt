package aoc2024

import kotlin.math.min

private const val DIR = "NESW"

fun main() {
    data class Point(val x: Int, val y: Int)

    val grid = generateSequence(::readLine).flatMapIndexed { y, s -> s.mapIndexed { x, c -> Point(x, y) to c } }.toMap()

    data class State(val pos: Point, val facing: Char) {
        fun next() = when {
            grid[pos] == 'E' -> sequenceOf()
            else -> sequenceOf(forward(), turn(3), turn(1)).filterNotNull()
        }

        fun forward() = when (facing) {
            'N' -> copy(pos = pos.copy(y = pos.y - 1))
            'E' -> copy(pos = pos.copy(x = pos.x + 1))
            'S' -> copy(pos = pos.copy(y = pos.y + 1))
            'W' -> copy(pos = pos.copy(x = pos.x - 1))
            else -> throw IllegalArgumentException(facing.toString())
        }.takeIf { grid[it.pos].let { it != null && it in ".E" } }

        fun turn(d: Int) = copy(facing = DIR[(DIR.indexOf(facing) + d) % DIR.length]).takeIf { it.forward() != null }
    }

    var curr = State(grid.filterValues { it == 'S' }.keys.single(), 'E')

    val V = mutableSetOf(curr)
    val D = mutableMapOf(curr to 0)
    val U = mutableSetOf<State>()
    val P = mutableMapOf<State, Set<State>>()

    var shortestPath = Int.MAX_VALUE
    while (true) {
        curr.next().filter { it !in V }.forEach {
            val cost = D.getValue(curr) + when {
                curr.facing == it.facing -> 1
                else -> 1000
            }
            if (cost <= shortestPath) {
                U += it
                val prevCost = D[it]
                D.compute(it) { _, old -> min(old ?: Int.MAX_VALUE, cost) }
                when {
                    prevCost == null || cost < prevCost -> P[it] = setOf(curr)
                    cost == prevCost -> P[it] = P.getValue(it) + curr
                }
            }
        }

        V += curr
        U -= curr

        if (grid[curr.pos] == 'E')
            shortestPath = min(shortestPath, D.getValue(curr))

        curr = U.minByOrNull { D.getValue(it) } ?: break
    }

    println(shortestPath)

    with(P.filterKeys { grid[it.pos] == 'E' }.keys.toMutableSet()) {
        while (true) if (!addAll(mapNotNull { P[it] }.flatten())) break
        println(map { it.pos }.toSet().size)
    }
}
