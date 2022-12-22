package aoc2022

import kotlin.math.max
import kotlin.math.min

fun main() {

    data class Configuration(
        val oreForOreRobot: Int,
        val oreForClayRobot: Int,
        val oreForObsidianRobot: Int,
        val clayForObsidianRobot: Int,
        val oreForGeodeRobot: Int,
        val obsidianForGeodeRobot: Int
    ) {
        fun maxNewOreRobot(ore: Int) = min(1, ore / oreForOreRobot)
        fun maxNewClayRobot(ore: Int) = min(1, ore / oreForClayRobot)
        fun maxNewObsidianRobot(ore: Int, clay: Int) =
            min(1, min(ore / oreForObsidianRobot, clay / clayForObsidianRobot))

        fun maxNewGeodeRobot(ore: Int, obsidian: Int) =
            min(1, min(ore / oreForGeodeRobot, obsidian / obsidianForGeodeRobot))
    }

    val blueprints = generateSequence(::readlnOrNull).map { line ->
        line.split(" ").mapNotNull(String::toIntOrNull).let {
            Configuration(it[0], it[1], it[2], it[3], it[4], it[5])
        }
    }.toList()

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
            return "$minute: $ore ($oreRobots) $clay ($clayRobots) $obsidian ($obsidianRobots) $geode ($geodeRobots)"
        }

        fun nextAll(): List<State> {
            return buildList {
                var buildingRobot = false
                if (cfg.maxNewOreRobot(ore) > 0 && oreRobots < max(cfg.oreForClayRobot, max(cfg.oreForObsidianRobot, cfg.oreForGeodeRobot))) {
                    buildingRobot = true
                    add(
                        copy(
                            minute = minute + 1,
                            ore = ore + oreRobots - cfg.oreForOreRobot,
                            clay = clay + clayRobots,
                            obsidian = obsidian + obsidianRobots,
                            geode = geode + geodeRobots,
                            oreRobots = oreRobots + 1,
                            clayRobots = clayRobots,
                            obsidianRobots = obsidianRobots,
                            geodeRobots = geodeRobots
                        )
                    )
                }
                if (cfg.maxNewClayRobot(ore) > 0 && clayRobots < cfg.clayForObsidianRobot) {
                    buildingRobot = true
                    add(
                        copy(
                            minute = minute + 1,
                            ore = ore + oreRobots - cfg.oreForClayRobot,
                            clay = clay + clayRobots,
                            obsidian = obsidian + obsidianRobots,
                            geode = geode + geodeRobots,
                            oreRobots = oreRobots,
                            clayRobots = clayRobots + 1,
                            obsidianRobots = obsidianRobots,
                            geodeRobots = geodeRobots
                        )
                    )
                }
                if (cfg.maxNewObsidianRobot(ore, clay) > 0 && obsidianRobots < cfg.obsidianForGeodeRobot) {
                    buildingRobot = true
                    add(
                        copy(
                            minute = minute + 1,
                            ore = ore + oreRobots - cfg.oreForObsidianRobot,
                            clay = clay + clayRobots - cfg.clayForObsidianRobot,
                            obsidian = obsidian + obsidianRobots,
                            geode = geode + geodeRobots,
                            oreRobots = oreRobots,
                            clayRobots = clayRobots,
                            obsidianRobots = obsidianRobots + 1,
                            geodeRobots = geodeRobots
                        )
                    )
                }
                if (cfg.maxNewGeodeRobot(ore, obsidian) > 0) {
                    buildingRobot = true
                    add(
                        copy(
                            minute = minute + 1,
                            ore = ore + oreRobots - cfg.oreForGeodeRobot,
                            clay = clay + clayRobots,
                            obsidian = obsidian + obsidianRobots - cfg.obsidianForGeodeRobot,
                            geode = geode + geodeRobots,
                            oreRobots = oreRobots,
                            clayRobots = clayRobots,
                            obsidianRobots = obsidianRobots,
                            geodeRobots = geodeRobots + 1
                        )
                    )
                }
                if (!buildingRobot) {
                    add(
                        copy(
                            minute = minute + 1,
                            ore = ore + oreRobots,
                            clay = clay + clayRobots,
                            obsidian = obsidian + obsidianRobots,
                            geode = geode + geodeRobots,
                            oreRobots = oreRobots,
                            clayRobots = clayRobots,
                            obsidianRobots = obsidianRobots,
                            geodeRobots = geodeRobots
                        )
                    )
                }
            }
        }
    }

    fun simulate(s: State, minutes: Int, cache: MutableMap<State, Int> = mutableMapOf()): Int? {
        if (s.minute == minutes) {
            //println(s)
            return s.geode
        }
        val cached = cache[s]
        if (cached != null) return cached
        return s.nextAll().maxOfOrNull { s2 -> simulate(s2, minutes, cache)?.also { cache[s2] = it } ?: 0 }
    }

    fun List<Configuration>.part1(): Int {
        val stats = mutableMapOf<Int, Int>()
        for ((index, cfg) in withIndex()) {
            val max = simulate(State(cfg), 24) ?: 0
            println(max)
            stats[index] = max
        }
        return stats.entries.sumOf { (it.key + 1) * it.value }
    }

    fun List<Configuration>.part2(): Int {
        val stats = mutableMapOf<Int, Int>()
        for ((index, cfg) in take(3).withIndex()) {
            val max = simulate(State(cfg), 32) ?: 0
            println(max)
            stats[index] = max
        }
        return stats.values.reduce(Int::times)
    }

    println(listOf(Configuration(4, 2, 3, 14, 2, 7), Configuration(2, 3, 3, 8, 3, 12)).part2())

    println(blueprints.part1())
    println(blueprints.part2())
}

