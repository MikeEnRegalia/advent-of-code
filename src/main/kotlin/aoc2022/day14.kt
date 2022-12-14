package aoc2022

import kotlin.math.max
import kotlin.math.min

fun main() = day14(String(System.`in`.readAllBytes())).forEach(::println)

private fun day14(input: String): List<Any?> {
    data class Pos(val x: Int, val y: Int) {
        val below by lazy { copy(y = y + 1) }
        val diagLeft by lazy { copy(x = x - 1, y = y + 1) }
        val diagRight by lazy { copy(x = x + 1, y = y + 1) }
    }

    val rock = buildSet {
        input.lines().map { it.split(" -> ").map { it.split(",").map(String::toInt) } }.forEach {
            it.windowed(2).forEach { (from, to) ->
                for (x in min(from[0], to[0])..max(from[0], to[0])) {
                    for (y in min(from[1], to[1])..max(from[1], to[1])) {
                        add(Pos(x, y))
                    }
                }
            }
        }
    }

    val floor = rock.maxOf { it.y } + 2

    val sandFrom = Pos(500, 0)

    fun simulate(haveFloor: Boolean): Int {
        val restingSand = mutableSetOf<Pos>()
        fun Pos.aboveFloor() = !haveFloor || y < floor

        var s: Pos = sandFrom
        while (true) {
            when {
                !haveFloor -> if (s.y >= floor) break
                else -> if (sandFrom in restingSand) break
            }

            when {
                s.below !in rock && s.below !in restingSand && s.below.aboveFloor() -> s = s.below
                s.diagLeft !in rock && s.diagLeft !in restingSand && s.below.aboveFloor() -> s = s.diagLeft
                s.diagRight !in rock && s.diagRight !in restingSand && s.below.aboveFloor() -> s = s.diagRight
                else -> {
                    restingSand += s
                    s = sandFrom
                }
            }
        }
        return restingSand.size
    }

    return listOf(simulate(false), simulate(true))
}

