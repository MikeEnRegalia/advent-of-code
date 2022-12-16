package aoc2022

import util.remove

fun main() = day16(String(System.`in`.readAllBytes())).forEach(::println)

private fun day16(input: String): List<Any?> {
    data class Valve(val name: String, val rate: Int, val to: List<String>, val open: Boolean = false)

    val flowRates = mutableMapOf<String, Int>()
    val tunnels = mutableMapOf<String, Set<String>>()
    input.lines().map { it.split(" ") }.forEach { tokens ->
        val name = tokens[1]
        val rate = tokens[4].substringAfterLast("=").remove(';').toInt()
        val to = tokens.subList(9, tokens.size).map { it.remove(',').trim() }.filter { it.isNotBlank() }
        flowRates[name] = rate
        tunnels[name] = to.toSet()
    }

    var max = 0
    fun findMax(
        valve: String,
        elephantValve: String? = null,
        minute: Int = 0,
        acc: Int = 0,
        open: Set<String> = setOf(),
        path: List<String> = listOf(valve),
        elephantPath: List<String> = listOfNotNull(elephantValve)
    ): Int {
        if (minute == 30) {
            if (acc > max) {
                max = acc
            }
            return acc
        }
        val rate = open.sumOf { flowRates.getValue(it) }
        if (acc + rate + (29 - minute) * flowRates.values.sum() < max) {
            return 0
        }

        fun nextValves(valve :String, path: List<String>) = tunnels.getValue(valve)
            .sortedByDescending { flowRates.getValue(it) }
            .filterNot { it in path }

        val next = buildList {
            nextValves(valve, path + elephantPath)
                .map { findMax(it, elephantValve, minute + 1, acc + rate, open, path + it) }.forEach(::add)

            if (flowRates.getValue(valve) > 0 && valve !in open)
                add(findMax(valve, elephantValve, minute + 1, acc + rate, open + valve))
        }
        return next.maxOfOrNull { it } ?: (acc + (30 - minute) * rate)
    }

    return listOf(findMax("AA"), null)
}

