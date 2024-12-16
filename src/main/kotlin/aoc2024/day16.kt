package aoc2024

import kotlin.math.min

private const val DIRECTIONS = "NESW"

fun main() {
    data class Point(val x: Int, val y: Int)

    val grid = generateSequence(::readLine).flatMapIndexed { y, s -> s.mapIndexed { x, c -> Point(x, y) to c } }.toMap()

    data class State(val pos: Point, val facing: Char) {
        fun next() = sequenceOf(forward(), turnLeft(), turnRight()).filter { grid.getValue(it.pos) in ".SE" }
        fun forward() = when (facing) {
            'N' -> copy(pos = pos.copy(y = pos.y - 1))
            'E' -> copy(pos = pos.copy(x = pos.x + 1))
            'S' -> copy(pos = pos.copy(y = pos.y + 1))
            'W' -> copy(pos = pos.copy(x = pos.x - 1))
            else -> throw IllegalArgumentException(facing.toString())
        }

        fun turnLeft() = copy(facing = DIRECTIONS[(DIRECTIONS.indexOf(facing) - 1 + 4) % 4])
        fun turnRight() = copy(facing = DIRECTIONS[(DIRECTIONS.indexOf(facing) + 1) % 4])
    }

    fun cost(s1: State, s2: State) = when {
        s1.facing == s2.facing -> 1
        else -> 1000
    }

    fun part1(): Int? {
        var curr = State(grid.filterValues { it == 'S' }.keys.single(), 'E')
        val V = mutableSetOf(curr)
        val D = mutableMapOf(curr to 0)

        while (true) {
            curr.next().filter { it !in V }.forEach {
                D.compute(it) { _, old -> min(old ?: Int.MAX_VALUE, D.getValue(curr) + cost(curr, it)) }
            }
            V += curr
            if (grid[curr.pos] == 'E') return D.getValue(curr)
            curr = D.filterKeys { it !in V}.minByOrNull { it.value }?.key ?: return null
        }
    }

    println(part1())
}
