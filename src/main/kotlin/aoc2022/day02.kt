package aoc2022

fun main() = day02(String(System.`in`.readAllBytes())).forEach(::println)

fun day02(input: String): List<Any?> {
    fun String.toRPS() = when (this) {
        "A", "X" -> "R"
        "B", "Y" -> "P"
        "C", "Z" -> "S"
        else -> throw IllegalArgumentException(this)
    }

    val score = input.lines().map { it.split(" ") }.fold(0 to 0) { (part1, part2), (opp, you) ->
        val roundPart1 = when (you.toRPS()) {
            "R" -> 1
            "P" -> 2
            "S" -> 3
            else -> throw IllegalArgumentException(you)
        } + when (you.toRPS() to opp.toRPS()) {
            "R" to "S", "S" to "P", "P" to "R" -> 6
            "R" to "R", "S" to "S", "P" to "P" -> 3
            "R" to "P", "S" to "R", "P" to "S" -> 0
            else -> throw IllegalArgumentException(you)
        }
        val roundPart2 = when (you) {
            "X" -> when (opp.toRPS()) {
                "R" -> 3
                "S" -> 2
                else -> 1
            }

            "Y" -> when (opp.toRPS()) {
                "R" -> 1 + 3
                "P" -> 2 + 3
                else -> 3 + 3
            }

            else -> when (opp.toRPS()) {
                "R" -> 2 + 6
                "S" -> 1 + 6
                else -> 3 + 6
            }
        }
        part1 + roundPart1 to part2 + roundPart2
    }

    return listOf(score.first, score.second)
}

