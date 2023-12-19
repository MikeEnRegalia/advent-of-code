package aoc2023

fun main() {
    val limits = mapOf("red" to 12, "green" to 13, "blue" to 14)

    data class Draw(val color: String, val n: Int)

    val games = generateSequence(::readLine).toList().map { line ->
        val game = line.split(" ")[1].replace(":", "").toInt()
        val maxValues = line.substring(line.indexOf(":") + 2).split(";").flatMap { part ->
            part.split(",").map { it.trim() }.map {
                val (nS, color) = it.split(" ")
                val n = nS.toInt()
                Draw(color, n)
            }
        }.groupBy { it.color }.mapValues { (k, v) -> v.maxOf { it.n } }

        val part1 = game.takeUnless {
            limits.any { (color, maxAllowed) ->
                val maxEncountered = maxValues[color]
                maxEncountered != null && maxEncountered > maxAllowed
            }
        } ?: 0
        part1 to maxValues.values.reduce(Int::times)
    }
    listOf(games.sumOf { it.first }, games.sumOf { it.second }).forEach(::println)
}

