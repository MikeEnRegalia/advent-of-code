package aoc2022

import kotlin.Int.Companion.MAX_VALUE
import kotlin.math.min

fun main() = day12(String(System.`in`.readAllBytes())).forEach(::println)

private fun day12(input: String): List<Any?> {

    data class State(val x: Int, val y: Int)

    val grid = input.lines().foldIndexed(mutableMapOf<State, Char>()) { y, acc, line ->
        line.forEachIndexed { x, c -> acc[State(x, y)] = c }
        acc
    }

    fun State.neighbors() = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1).map { State(x + it.first, y + it.second) }
        .filter { it in grid }
        .filter { grid.getValue(it) <= grid.getValue(this) + 1 }

    val startPart1 = grid.entries.single { it.value == 'S' }.key
    val target = grid.entries.single { it.value == 'E' }.key
    grid[startPart1] = 'a'
    grid[target] = 'z'

    val shortestPaths = mutableMapOf<State, Int>()
    grid.filter { it.value == 'a' }.keys.forEach { start ->
        var s = start
        val v = mutableSetOf<State>()
        val u = mutableSetOf<State>()
        val d = mutableMapOf(s to 0)

        while (true) {
            s.neighbors().filter { it !in v }.forEach { n ->
                u += n
                val distance = d.getValue(s) + 1
                d.compute(n) { _, old -> min(distance, old ?: MAX_VALUE) }
            }
            if (s == target) {
                shortestPaths[start] = d.getValue(s)
                break
            }
            v += s
            u -= s
            s = u.minByOrNull { d.getValue(it) } ?: break
        }
    }
    return listOf(shortestPaths[startPart1], shortestPaths.values.min())
}