package aoc2023

import kotlin.math.min

fun main() {
    val grid = generateSequence(::readLine).toList()
    val (xRange, yRange) = grid[0].indices to grid.indices

    data class Pos(val x: Int, val y: Int)

    fun Pos.neighbors() = listOf(copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1))
        .filter { it.x in xRange && it.y in yRange }

    data class State(val pos: Pos, val prev: Pos? = null, val straightFor: Int = 0)

    fun Set<Pos>.onStraightLine() = all { it.x == first().x } || all { it.y == first().y }

    fun State.next() = pos.neighbors()
        .filter { it != prev }
        .map { State(it, prev = pos, straightFor = if (setOfNotNull(it, pos, prev).onStraightLine()) straightFor + 1 else 1 ) }
        .filter { it.straightFor <= 3 }

    fun State.ultraNext() = pos.neighbors()
        .filter { it != prev }
        .filter {
            if (straightFor >= 4) true else setOfNotNull(it, pos, prev).onStraightLine()
        }
        .map { State(it, prev = pos, straightFor = if (setOfNotNull(it, pos, prev).onStraightLine()) straightFor + 1 else 1 ) }
        .filter { it.straightFor <= 10 }

    fun solve(t: (State) -> List<State>) {
        val V = mutableSetOf<State>()
        var curr = State(Pos(0, 0))
        val D = mutableMapOf(curr to 0)
        val N = mutableSetOf<State>()

        while (true) {
            t(curr).filter { it !in V }.forEach {
                val d = D.getValue(curr) + grid[it.pos.y][it.pos.x].digitToInt()
                if (d > xRange.last * 2 * 9) return@forEach
                D.compute(it) { _, old -> min(old ?: Int.MAX_VALUE, d) }
                N += it
            }
            curr = N.minByOrNull { D.getValue(it) } ?: break
            V += curr
            N -= curr
            if (curr.pos.let { it.x == xRange.last && it.y == yRange.last }) {
                println(D[curr])
                break
            }
        }
    }

    solve(State::next)
    solve(State::ultraNext)
}