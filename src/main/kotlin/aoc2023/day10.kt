package aoc2023

import kotlin.math.min

fun main() = day10(generateSequence(::readlnOrNull).toList()).forEach(::println)

private fun day10(lines: List<String>): List<Any?> {
    data class Pos(val x: Int, val y: Int) {
        val c: Char by lazy {
            when {
                !inGrid -> 'X'
                else -> lines[y][x].takeIf { it != 'S' } ?: when {
                    copy(x = x + 1).c in fromWest && copy(x = x - 1).c in fromEast -> '-'
                    copy(y = y + 1).c in fromNorth && copy(y = y - 1).c in fromSouth -> '|'
                    copy(x = x + 1).c in fromWest && copy(y = y + 1).c in fromSouth -> 'F'
                    copy(x = x + 1).c in fromWest && copy(y = y - 1).c in fromNorth -> 'L'
                    copy(x = x - 1).c in fromEast && copy(y = y + 1).c in fromSouth -> '7'
                    copy(x = x - 1).c in fromEast && copy(y = y - 1).c in fromNorth -> 'J'
                    else -> '?'
                }
            }
        }
        private val fromWest = "7-J"
        private val fromEast = "F-L"
        private val fromNorth = "L|J"
        private val fromSouth = "F|7"
        val inGrid = x in lines[0].indices && y in lines.indices
        fun neighbors() = listOf(copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1))
            .filter { it.inGrid }

        fun pipeNeighbors() = neighbors()
            .filter {
                val dx = it.x - x
                val dy = it.y - y
                when {
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

    val path = mutableListOf(start)
    while (true) {
        path += path.last().pipeNeighbors().filter { it !in path }.randomOrNull() ?: break
    }

    fun Pos.isTile() = c != '?' && this !in path

    val leftTiles = mutableSetOf<Pos>()
    val rightTiles = mutableSetOf<Pos>()
    for ((i, p) in path.withIndex()) {
        val prev = if (i == 0) path.last() else path[i - 1]
        val next = if (i == path.size - 1) path.first() else path[i + 1]
        val dx = next.x - prev.x
        val dy = next.y - prev.y

        fun MutableSet<Pos>.addIfTile(p: Pos, dx: Int = 0, dy: Int = 0) = p.copy(x = p.x + dx, y = p.y + dy)
            .let { if (it.isTile()) add(it) else null }

        when {
            p.c == '|' -> {
                (if (dy > 0) rightTiles else leftTiles).addIfTile(p, dx = -1)
                (if (dy > 0) leftTiles else rightTiles).addIfTile(p, dx = 1)
            }

            p.c == '-' -> {
                (if (dx > 0) rightTiles else leftTiles).addIfTile(p, dy = 1)
                (if (dx > 0) leftTiles else rightTiles).addIfTile(p, dy = -1)
            }

            p.c == 'F' -> {
                (if (dx > 0) rightTiles else leftTiles).addIfTile(p, dx = 1, dy = 1)
                (if (dx > 0) leftTiles else rightTiles).addIfTile(p, dx = 0, dy = -1)
                (if (dx > 0) leftTiles else rightTiles).addIfTile(p, dx = -1, dy = -1)
                (if (dx > 0) leftTiles else rightTiles).addIfTile(p, dx = -1, dy = 0)
            }

            p.c == '7' -> {
                (if (dx > 0) rightTiles else leftTiles).addIfTile(p, dx = -1, dy = 1)
                (if (dx > 0) leftTiles else rightTiles).addIfTile(p, dx = 0, dy = -1)
                (if (dx > 0) leftTiles else rightTiles).addIfTile(p, dx = 1, dy = -1)
                (if (dx > 0) leftTiles else rightTiles).addIfTile(p, dx = 1, dy = 0)
            }

            p.c == 'J' -> {
                (if (dx > 0) leftTiles else rightTiles).addIfTile(p, dx = -1, dy = -1)
                (if (dx > 0) rightTiles else leftTiles).addIfTile(p, dx = 1, dy = 0)
                (if (dx > 0) rightTiles else leftTiles).addIfTile(p, dx = 1, dy = 1)
                (if (dx > 0) rightTiles else leftTiles).addIfTile(p, dx = 0, dy = 1)
            }

            p.c == 'L' -> {
                (if (dx > 0) leftTiles else rightTiles).addIfTile(p, dx = 1, dy = -1)
                (if (dx > 0) rightTiles else leftTiles).addIfTile(p, dx = 0, dy = 1)
                (if (dx > 0) rightTiles else leftTiles).addIfTile(p, dx = -1, dy = 1)
                (if (dx > 0) rightTiles else leftTiles).addIfTile(p, dx = -1, dy = 0)
            }
        }
    }

    val tiles = listOf(leftTiles, rightTiles).map { seeds ->
        val toFollow = seeds.toMutableList()
        val tiles = seeds.toMutableSet()
        while (toFollow.isNotEmpty()) {
            toFollow.removeLast().neighbors().filter { it.isTile() }.forEach {
                if (it !in tiles) {
                    toFollow += it
                    tiles += it
                }
            }
        }
        tiles
    }
        .singleOrNull { tiles ->
            tiles.none { it.x == 0 || it.y == 0 || it.x == lines[0].indices.last || it.y == lines.indices.last }
        }



    return listOf(part1, tiles?.size)
}

