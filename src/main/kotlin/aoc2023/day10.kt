package aoc2023

import javax.swing.text.html.HTML.Tag.S
import kotlin.math.min

fun main() = day10b(generateSequence(::readlnOrNull).toList()).forEach(::println)

private fun day10b(lines: List<String>): List<Any?> {
    data class Pos(val x: Int, val y: Int) {
        private val fromWest = "7-J"
        private val fromEast = "F-L"
        private val fromNorth = "L|J"
        private val fromSouth = "F|7"
        fun neighbors() = listOf(copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1))
            .filter { it.x in lines[0].indices && it.y in lines.indices }
            .filter {
                val c = lines[y][x]
                val c2 = lines[it.y][it.x]
                val dx = it.x - x
                val dy = it.y - y
                if (c == 'S') dx == 0 else when {
                    c == '-' && dy == 0 -> c2 in fromWest || c2 in fromEast
                    c == '|' && dx == 0 -> c2 in fromNorth || c2 in fromSouth

                    c == 'F' && dx == 1 -> c2 in fromWest
                    c == 'F' && dy == 1 -> c2 in fromNorth

                    c == '7' && dx == -1 -> c2 in fromEast
                    c == '7' && dy == 1 -> c2 in fromNorth

                    c == 'J' && dx == -1 -> c2 in fromEast
                    c == 'J' && dy == -1 -> c2 in fromSouth

                    c == 'L' && dx == 1 -> c2 in fromWest
                    c == 'L' && dy == -1 -> c2 in fromSouth
                    else -> false
                }
            }

    }

    val start = lines.indexOfFirst { 'S' in it }.let { y -> Pos(lines[y].indexOf('S'), y) }
    val D = mutableMapOf(start to 0)
    val V = mutableSetOf(start)

    var curr = start
    while (true) {
        curr.neighbors().filter { it !in V }.forEach {
            val d = D.getValue(curr) + 1
            D.compute(it) { _, old -> min(old ?: Int.MAX_VALUE, d) }
        }
        V += curr
        val remaining = D.keys - V
        curr = remaining.toList().minByOrNull { D.getValue(it) } ?: break
    }

    val pipes = V.toSet()
    val part1 = D.values.max()

    D.clear()
    V.clear()

    return listOf(part1)
}