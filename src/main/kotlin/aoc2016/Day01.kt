package aoc2016

import kotlin.math.abs

fun main() {

    data class Pos(val x: Int, val y: Int, val facing: String) {
        fun turn(dir: String) = (if (dir == "R") "NESW" else "WSEN").let { dirs ->
            copy(facing = dirs[(dirs.indexOf(facing) + 1) % dirs.length].toString())
        }

        fun move(d: Int) = when (facing) {
            "N" -> (y downTo y - d).map { copy(y = it) }
            "S" -> (y..y + d).map { copy(y = it) }
            "W" -> (x downTo x - d).map { copy(x = it) }
            "E" -> (x..x + d).map { copy(x = it) }
            else -> throw IllegalStateException(facing)
        }.drop(1)

        fun distance() = abs(x) + abs(y)
    }

    fun solve(line: String) {
        val input = line.split(Regex(", ")).map { it.substring(0, 1) to it.substring(1).toInt() }
        val seen = mutableSetOf<Pair<Int, Int>>()
        var seenTwice: Pos? = null

        val finalPos = input.fold(Pos(0, 0, "N")) { pos, (dir, d) ->
            pos.turn(dir).move(d).run {
                forEach {
                    if (seenTwice == null && !seen.add(it.x to it.y)) {
                        seenTwice = it
                    }
                }
                last()
            }
        }

        println(finalPos.distance())
        println(seenTwice?.distance())
    }

    solve(readln())
}