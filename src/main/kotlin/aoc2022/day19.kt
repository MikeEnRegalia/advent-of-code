package aoc2022

import kotlin.math.min

fun main() = day19(String(System.`in`.readAllBytes())).forEach(::println)

private fun day19(input: String): List<Any?> {
    val blueprints = input.lines().map { it.split(" ").mapNotNull(String::toIntOrNull)}

    fun calc(bp: List<Int>): Int {
        val (oreForOre, oreForClay, oreForObs, clayForObs, oreForG) = bp
        val obsForG = bp[5]
        var ore = 0
        var oreRobot = 1
        var clay = 0
        var clayRobot = 0
        var obs = 0
        var obsRobot = 0
        var g = 0
        var gRobot = 0
        for (minute in 1..24) {
            ore += oreRobot
            clay += clayRobot
            obs += obsRobot
            g += gRobot
            val makeG = min(ore/oreForG, obs/obsForG)
            if (makeG > 0) {
                gRobot += makeG
                ore -= makeG * oreForG
                obs -= makeG * obsForG
            }
            val makeObs = min(ore/oreForObs, clay/clayForObs)
            if (makeObs > 0) {
                obsRobot += makeObs
                ore -= makeObs * oreForObs
                clay -= makeObs * clayForObs
            }
            val makeClayRobot = ore/oreForClay
            if (makeClayRobot > 0) {
                clayRobot += makeClayRobot
                ore -= makeClayRobot * oreForClay
            }
            val makeOreRobot = ore/oreForOre
            if (makeOreRobot > 0) {
                oreRobot += makeOreRobot
                ore -= makeOreRobot * oreForOre
            }
            println("$minute: $oreRobot ($ore) $clayRobot ($clay) $obsRobot ($obs) $gRobot ($g)")
        }
        return g
    }

    calc(listOf(4, 2, 3, 14, 2, 7)).also(::println)

    val stats = mutableMapOf<Int, Int>()
    for ((bpi, bp) in blueprints.withIndex()) stats[bpi] = calc(bp)

    return listOf(stats.entries.sumOf { (it.key + 1) * it.value }, null)
}

