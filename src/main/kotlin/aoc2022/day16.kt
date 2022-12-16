package aoc2022

import util.remove
import kotlin.Int.Companion.MAX_VALUE
import kotlin.math.min

fun main() = day16(String(System.`in`.readAllBytes())).forEach(::println)

private fun day16(input: String): List<Any?> {
    val flowRates = mutableMapOf<String, Int>()
    val tunnels = mutableMapOf<String, Set<String>>()
    input.lines().map { it.split(" ") }.forEach { tokens ->
        val name = tokens[1]
        val rate = tokens[4].substringAfterLast("=").remove(';').toInt()
        val to = tokens.subList(9, tokens.size).map { it.remove(',').trim() }.filter { it.isNotBlank() }
        flowRates[name] = rate
        tunnels[name] = to.toSet()
    }

    val functioningValves = flowRates.filter { it.value > 0 }.keys

    fun whereToNext(valve: String, remainingValves: Set<String>) = buildSet {
        for (target in remainingValves.minus(valve)) {
            data class State(val valve: String, val path: List<String> = listOf())

            var s = State(valve)
            val v = mutableSetOf<State>()
            val u = mutableSetOf<State>()
            val d = mutableMapOf(s to 0)

            while (true) {
                tunnels.getValue(s.valve)
                    .map { State(it, s.path + it) }
                    .filter { n -> v.none { it.valve == n.valve } }.forEach { n ->
                        u += n
                        val distance = d.getValue(s) + 1
                        d.compute(n) { _, old -> min(distance, old ?: MAX_VALUE) }
                    }
                if (s.valve == target) {
                    add(target to s.path.size + 1)
                    break
                }
                v += s
                u -= s
                s = u.minByOrNull { d.getValue(it) } ?: break
            }
        }
    }

    data class Helper(val valve: String, val minutesSpent: Int = 1)
    data class OpenedValve(val valve: String, val openedInMinute: Int) {
        override fun toString(): String {
            return "$valve:$openedInMinute"
        }
    }

    fun Iterable<OpenedValve>.pressureReleased(deadline: Int) =
        (1..deadline).sumOf { minute ->
            filter { it.openedInMinute <= minute }.sumOf { flowRates.getValue(it.valve) }
        }

    var max = 0
    fun openValves(deadline: Int, helpers: List<Helper>, openedValves: List<OpenedValve> = listOf()): Int {
        val pressureReleased = openedValves.pressureReleased(deadline)

        return buildList {
            for (helper in helpers) {
                val nextValves = functioningValves.minus(openedValves.map { it.valve }.toSet())
                val moves = whereToNext(helper.valve, nextValves)
                    .filter { (_, distance) -> helper.minutesSpent + distance <= deadline }

                moves.forEach { (valve, distance) ->
                    val newHelper = Helper(valve, helper.minutesSpent + distance)
                    val openedValve = OpenedValve(valve, helper.minutesSpent + distance)
                    add(openValves(deadline, helpers - helper + newHelper, openedValves + openedValve))
                }
            }
        }.maxOfOrNull { it } ?: pressureReleased.also {
            if (it >= max) {
                max = it
            }
        }
    }

    val part1 = openValves(30, listOf(Helper("AA")))
    println(part1)

    max = 0

    val part2 = openValves(26, listOf(Helper("AA"), Helper("AA")))
    println(part2)

    return listOf()
}