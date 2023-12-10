package aoc2023

import kotlin.math.min

fun main() = day10(generateSequence(::readlnOrNull).toList()).forEach(::println)

private fun day10(lines: List<String>): List<Any?> {
    data class Pos(val x: Int, val y: Int) {
        val c by lazy { lines[y][x] }
        private val fromWest = "7-J"
        private val fromEast = "F-L"
        private val fromNorth = "L|J"
        private val fromSouth = "F|7"
        fun neighbors() = listOf(copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1))
            .filter { it.x in lines[0].indices && it.y in lines.indices }

        fun pipeNeighbors() = neighbors()
            .filter {
                val dx = it.x - x
                val dy = it.y - y
                if (c == 'S') dx == 0 else when {
                    c == '-' && dy == 0 -> it.c in fromWest || it.c in fromEast
                    c == '|' && dx == 0 -> it.c in fromNorth || it.c in fromSouth

                    c == 'F' && dx == 1 -> it.c in fromWest
                    c == 'F' && dy == 1 -> it.c in fromNorth

                    c == '7' && dx == -1 -> it.c in fromEast
                    c == '7' && dy == 1 -> it.c in fromNorth

                    c == 'J' && dx == -1 -> it.c in fromEast
                    c == 'J' && dy == -1 -> it.c in fromSouth

                    c == 'L' && dx == 1 -> it.c in fromWest
                    c == 'L' && dy == -1 -> it.c in fromSouth
                    else -> false
                }
            }

    }

    val start = lines.indexOfFirst { 'S' in it }.let { y -> Pos(lines[y].indexOf('S'), y) }
    val D = mutableMapOf(start to 0)
    val V = mutableSetOf(start)

    var curr = start
    while (true) {
        curr.pipeNeighbors().filter { it !in V }.forEach {
            val d = D.getValue(curr) + 1
            D.compute(it) { _, old -> min(old ?: Int.MAX_VALUE, d) }
        }
        V += curr
        val remaining = D.keys - V
        curr = remaining.toList().minByOrNull { D.getValue(it) } ?: break
    }

    val part1 = D.values.max()

    return listOf(part1)
}