package aoc2022

fun main() = day02(String(System.`in`.readAllBytes())).forEach(::println)

fun day02(input: String): List<Any?> {
    val R = "A"
    val P = "B"
    val S = "C"
    val R1 = "X"
    val P1 = "Y"
    val S1 = "Z"
    val score = input.lines().map { it.split(" ") }.fold(0 to 0) { (part1, part2), (opp, you) ->
        val roundPart1 = when (you) {
            R1 -> 1
            P1 -> 2
            S1 -> 3
            else -> throw IllegalArgumentException(you)
        } + when (you to opp) {
            R1 to S -> 6
            S1 to P -> 6
            P1 to R -> 6
            R1 to R -> 3
            S1 to S -> 3
            P1 to P -> 3
            R1 to P -> 0
            S1 to R -> 0
            P1 to S -> 0
            else -> 0
        }
        val roundPart2 = when (you) {
            "X" -> when (opp) {
                R -> 3
                S -> 2
                else -> 1
            }
            "Y" -> when (opp) {
                R -> 1 + 3
                P -> 2 + 3
                else -> 3 + 3
            }
            else -> when (opp) {
                R -> 2 + 6
                S -> 1 + 6
                else -> 3 + 6
            }
        }
        part1 + roundPart1 to part2 + roundPart2
    }

    return listOf(score.first, score.second)
}

