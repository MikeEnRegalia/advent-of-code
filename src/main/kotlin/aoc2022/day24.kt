package aoc2022

import kotlin.math.abs
import kotlin.math.min

fun main() = day24(String(System.`in`.readAllBytes())).forEach(::println)

private fun day24(input: String): List<Any?> {
    data class Pos(val x: Int, val y: Int) {
        fun dist(pos: Pos) = abs(x - pos.x) + abs(y - pos.y)
    }

    data class Blizzard(val pos: Pos, val dir: Char)

    val startBlizzards = mutableListOf<Blizzard>()
    val walls = input.lines().flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if (c in "><^v") startBlizzards += Blizzard(Pos(x, y), c)
            if (c == '#') Pos(x, y) else null
        }
    }

    val topWall = 0
    val leftWall = 0
    val rightWall = walls.maxOf { it.x }
    val bottomWall = walls.maxOf { it.y }

    fun List<Blizzard>.next() = map { (pos, dir) ->
        with(pos) {
            Blizzard(
                when (dir) {
                    '>' -> Pos(if (x == rightWall - 1) leftWall + 1 else x + 1, y)
                    '<' -> Pos(if (x == leftWall + 1) rightWall - 1 else x - 1, y)
                    '^' -> Pos(x, if (y == topWall + 1) bottomWall - 1 else y - 1)
                    else -> Pos(x, if (y == bottomWall - 1) topWall + 1 else y + 1)
                }, dir
            )
        }
    }

    val blizzardCache = mutableMapOf<Int, List<Blizzard>>()
    blizzardCache[0] = startBlizzards

    val startPos = Pos(1, 0)

    data class State(
        val pos: Pos,
        val minute: Int = 0,
        val visitedExit: Boolean = false,
        val visitedStart: Boolean = false,
        val visitedExitAgain: Boolean = false
    ) {

        fun neighbors(): Sequence<State> {
            val newBlizzards = blizzardCache[minute] ?: blizzardCache.getValue(minute - 1).next().also { blizzardCache[minute] = it }
            return sequenceOf(
                Pos(pos.x + 1, pos.y), Pos(pos.x, pos.y + 1),
                pos,
                Pos(pos.x - 1, pos.y), Pos(pos.x, pos.y - 1)
            )
                .filter { it !in walls && it.y >= 0 && it.y <= bottomWall  }
                .filter { pos -> newBlizzards.none { it.pos == pos } }
                .map {
                    State(
                        it,
                        minute + 1,
                        visitedExit = visitedExit || it.y == bottomWall,
                        visitedStart = visitedStart || (visitedExit && it.y == 0),
                        visitedExitAgain = visitedExitAgain || visitedStart && it.y == bottomWall
                    )
                }
        }
    }

    var s = State(startPos)

    val v = mutableSetOf<State>()
    val u = mutableSetOf<State>()
    val d = mutableMapOf(s to 0)

    var max = 0
    var visitedExitOnce = false
    while (true) {
        s.neighbors().filter { it !in v }.forEach { n ->
            u += n
            val distance = d.getValue(s) + 1
            if (distance > max) {
                max = distance
            }
            d.compute(n) { _, old -> min(distance, old ?: Int.MAX_VALUE) }
        }
        if (!visitedExitOnce && s.visitedExit) {
            println(d.getValue(s))
            visitedExitOnce = true
        }
        if (s.visitedExitAgain) {
            println(d.getValue(s))
            break
        }
        v += s
        u -= s
        s = u.minByOrNull { d.getValue(it) } ?: break
    }

    return listOf(null, null)
}

