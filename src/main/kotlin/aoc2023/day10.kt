package aoc2023

import kotlin.math.min

fun main() = day10(generateSequence(::readlnOrNull).toList()).forEach(::println)

private const val fromWest = "7-J"
private const val fromEast = "F-L"
private const val fromNorth = "L|J"
private const val fromSouth = "F|7"

private fun day10(lines: List<String>): List<Any?> {
    data class Pos(val x: Int, val y: Int) {
        val inGrid by lazy { x in lines[0].indices && y in lines.indices }
        val atGridBorder by lazy { x == 0 || y == 0 || x == lines[0].indices.last || y == lines.indices.last }

        val c: Char by lazy {
            when {
                !inGrid -> 'X'
                else -> lines[y][x].takeIf { it != 'S' } ?: when {
                    d(dx = 1).c in fromWest && d(dx = -1).c in fromEast -> '-'
                    d(dy = 1).c in fromNorth && d(dy = -1).c in fromSouth -> '|'
                    d(dx = 1).c in fromWest && d(dy = 1).c in fromSouth -> 'F'
                    d(dx = 1).c in fromWest && d(dy = -1).c in fromNorth -> 'L'
                    d(dx = -1).c in fromEast && d(dy = 1).c in fromSouth -> '7'
                    d(dx = -1).c in fromEast && d(dy = -1).c in fromNorth -> 'J'
                    else -> '?'
                }
            }
        }

        fun d(dx: Int = 0, dy: Int = 0) = copy(x = x + dx, y = y + dy)

        fun neighbors() = sequenceOf(d(dx = 1), d(dx = -1), d(dy = 1), d(dy = -1)).filter { it.inGrid }

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
        curr = remaining.minByOrNull { D.getValue(it) } ?: break
    }

    val part1 = D.values.max()

    val path = mutableListOf(start)
    while (true)
        path += path.last().pipeNeighbors().filter { it !in path }.firstOrNull() ?: break

    fun Pos.isTile() = c != '?' && this !in path

    val leftTiles = mutableSetOf<Pos>()
    val rightTiles = mutableSetOf<Pos>()
    for ((i, p) in path.withIndex()) {
        val prev = if (i == 0) path.last() else path[i - 1]
        val next = if (i == path.size - 1) path.first() else path[i + 1]
        val dx = next.x - prev.x

        fun MutableSet<Pos>.addIfTile(dx: Int = 0, dy: Int = 0) = p.d(dx, dy)
            .let { if (it.isTile()) add(it) else null }

        val rightIfEastward = if (dx > 0) rightTiles else leftTiles
        val leftIfEastward = if (dx > 0) leftTiles else rightTiles

        when (p.c) {
            'F' -> {
                rightIfEastward.addIfTile(dx = 1, dy = 1)
                leftIfEastward.addIfTile(dx = 0, dy = -1)
                leftIfEastward.addIfTile(dx = -1, dy = -1)
                leftIfEastward.addIfTile(dx = -1, dy = 0)
            }

            '7' -> {
                rightIfEastward.addIfTile(dx = -1, dy = 1)
                leftIfEastward.addIfTile(dx = 0, dy = -1)
                leftIfEastward.addIfTile(dx = 1, dy = -1)
                leftIfEastward.addIfTile(dx = 1, dy = 0)
            }

            'J' -> {
                leftIfEastward.addIfTile(dx = -1, dy = -1)
                rightIfEastward.addIfTile(dx = 1, dy = 0)
                rightIfEastward.addIfTile(dx = 1, dy = 1)
                rightIfEastward.addIfTile(dx = 0, dy = 1)
            }

            'L' -> {
                leftIfEastward.addIfTile(dx = 1, dy = -1)
                rightIfEastward.addIfTile(dx = 0, dy = 1)
                rightIfEastward.addIfTile(dx = -1, dy = 1)
                rightIfEastward.addIfTile(dx = -1, dy = 0)
            }
        }
    }

    fun Set<Pos>.exploreTiles(): Set<Pos> {
        val toFollow = toMutableList()
        val tiles = toMutableSet()
        while (toFollow.isNotEmpty()) {
            toFollow.removeLast().neighbors().filter { it.isTile() }.forEach {
                if (it !in tiles) {
                    toFollow += it
                    tiles += it
                }
            }
        }
        return tiles
    }

    val tiles = listOf(leftTiles, rightTiles)
        .map(MutableSet<Pos>::exploreTiles)
        .singleOrNull { it.none(Pos::atGridBorder) }

    return listOf(part1, tiles?.size)
}

