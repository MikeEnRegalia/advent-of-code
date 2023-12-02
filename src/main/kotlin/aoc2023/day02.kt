package aoc2023

fun main() = day02(String(System.`in`.readAllBytes()).split("\n")).forEach(::println)

private fun day02(lines: List<String>): List<Any?> {

    val limits = mapOf("red" to 12, "green" to 13, "blue" to 14)

    val games = lines.map { line ->
        val game = line.split(" ")[1].replace(":", "").toInt()
        val maxValues = line.substring(line.indexOf(":") + 2).split(";").flatMap { part ->
            part.split(",").map { it.trim() }.map {
                val (nS, color) = it.split(" ")
                val n = nS.toInt()
                color to n
            }
        }.groupBy { it.first }.mapValues { (k, v) -> v.maxOf { it.second } }

        val part1 = game.takeUnless {
            limits.any { (color, maxAllowed) ->
                val maxEncountered = maxValues[color]
                maxEncountered != null && maxEncountered > maxAllowed
            }
        } ?: 0
        val part2 = maxValues.values.reduce(Int::times)
        part1 to part2
    }

    return listOf(games.sumOf { it.first }, games.sumOf { it.second })
}