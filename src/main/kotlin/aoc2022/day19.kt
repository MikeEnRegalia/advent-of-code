package aoc2022

import kotlin.math.min

fun main() = day19(String(System.`in`.readAllBytes())).forEach(::println)

private fun day19(input: String): List<Any?> {
    data class Configuration(
        val oreForOreRobot: Int,
        val oreForClayRobot: Int,
        val oreForObsidianRobot: Int,
        val clayForObsidianRobot: Int,
        val oreForGeodeRobot: Int,
        val obsidianForGeodeRobot: Int
    )

    val blueprints = input.lines().map { line ->
        line.split(" ").mapNotNull(String::toIntOrNull).let {
            Configuration(it[0], it[1], it[2], it[3], it[4], it[5])
        }
    }

    data class State(
        val cfg: Configuration,
        val minute: Int = 0,
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geode: Int = 0,
        val oreRobots: Int = 1,
        val clayRobots: Int = 0,
        val obsidianRobots: Int = 0,
        val geodeRobots: Int = 0
    ) {
        override fun toString(): String {
            return "$minute: $ore $clay $obsidian $geode"
        }
        fun maxNewOreRobot(ore: Int) = ore / cfg.oreForOreRobot
        fun maxNewClayRobot(ore: Int) = ore / cfg.oreForClayRobot
        fun maxNewObsidianRobot(ore: Int, clay: Int) = min(ore / cfg.oreForObsidianRobot, clay / cfg.clayForObsidianRobot)
        fun maxNewGeodeRobot(ore: Int, obsidian: Int) = min(ore / cfg.oreForGeodeRobot, obsidian / cfg.obsidianForGeodeRobot)

        fun next() = buildList {
            var ore = this@State.ore
            var clay = this@State.clay
            var obsidian = this@State.obsidian
            val geode = this@State.geode
            val newGeodeRobots = maxNewGeodeRobot(ore, obsidian)
            ore -= newGeodeRobots * cfg.oreForGeodeRobot
            obsidian -= newGeodeRobots * cfg.obsidianForGeodeRobot
            val needMoreObsidian = obsidianRobots/oreRobots < cfg.obsidianForGeodeRobot/cfg.oreForGeodeRobot
            val newObsidianRobots = if (needMoreObsidian) maxNewObsidianRobot(ore, clay) else 0
            ore -= newObsidianRobots * cfg.oreForObsidianRobot
            clay -= newObsidianRobots * cfg.clayForObsidianRobot
            val needMoreClayRobots = clayRobots/oreRobots < cfg.clayForObsidianRobot/cfg.oreForObsidianRobot
            val newClayRobots = if (needMoreClayRobots) maxNewClayRobot(ore) else 0
            ore -= newClayRobots * cfg.oreForClayRobot
            val newOreRobots = maxNewOreRobot(ore)
            ore -= newOreRobots * cfg.oreForOreRobot
            add(copy(
                minute = minute + 1,
                ore = ore + oreRobots,
                clay = clay + clayRobots,
                obsidian = obsidian + obsidianRobots,
                geode = geode + geodeRobots,
                oreRobots = oreRobots + newOreRobots,
                clayRobots = clayRobots + newClayRobots,
                obsidianRobots = obsidianRobots + newObsidianRobots,
                geodeRobots = geodeRobots + newGeodeRobots))
        }

    }

    fun simulate(s: State, cache: MutableMap<State, Int> = mutableMapOf()): Int? {
        println(s)
        if (s.minute == 24) {
            return s.geode
        }
        val cached = cache[s]
        if (cached != null) {
            //println("cached")
            return cached
        }
        return s.next().maxOfOrNull { s2 ->simulate(s2, cache)?.also { cache[s2] = it } ?: 0 }
    }

    println("x: " + simulate(State(Configuration(4, 2, 3, 14, 2, 7))))
    println("y: " + simulate(State(Configuration(2, 3, 3, 8, 3, 12))))

    fun part1(): Int {
        val stats = mutableMapOf<Int, Int>()
        for ((index, cfg) in blueprints.withIndex()) {
            val s = State(cfg)
            val max = simulate(s) ?: 0
            println(max)
            stats[index] = max
        }
        return stats.entries.sumOf { (it.key + 1) * it.value }
    }


    return listOf(null, null)
}

