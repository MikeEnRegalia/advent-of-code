package aoc2023

fun main() = day02(String(System.`in`.readAllBytes()).split("\n")).forEach(::println)

private fun day02(lines: List<String>): List<Any?> {

    val limits = mapOf("red" to 12, "green" to 13, "blue" to 14)

    val sum = lines.map { line ->
        val game = line.split(" ")[1].replace(":", "").toInt()
        val maxValues = mutableMapOf<String, Int>()
        line.substring(line.indexOf(":") + 2).split(";").map { part ->
            part.split(",").map { it.trim() }.forEach {
                val (nS, color) = it.split(" ")
                val n = nS.toInt()
                val curr = maxValues[color]
                if (curr == null || curr < n) maxValues[color] = n
            }
        }
        val part1 = when {
            limits.any { (k, v) ->
                val max = maxValues[k]
                max != null && max > v
            } -> 0

            else -> game
        }
        val part2 = maxValues.values.reduce(Int::times)
        part1 to part2
    }

    return listOf(sum.sumOf { it.first }, sum.sumOf { it.second})
}