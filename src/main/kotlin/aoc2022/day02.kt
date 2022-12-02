package aoc2022

import aoc2022.RPSMove.*
import aoc2022.RPSResult.*
import aoc2022.RPSMove.values as RPSMoves
import aoc2022.RPSResult.values as RPSResults

fun main() = day02Clean(String(System.`in`.readAllBytes())).forEach(::println)

fun day02Clean(input: String) = with(input.lines().map { it.split(" ") }) {
    fun playPart1(line: List<String>): Int {
        val (opponentMove, myMove) = line.map { token -> RPSMoves().single { token in it.tokens } }
        val result = myMove.playAgainst(opponentMove)
        return myMove.score + result.score
    }

    fun playPart2(line: List<String>): Int {
        val opponentMove = RPSMoves().single { line[0] in it.tokens }
        val desiredResult = RPSResults().single { it.token == line[1] }
        val myMove = opponentMove.whatToPlayToAchieve(desiredResult)
        return myMove.score + desiredResult.score
    }
    listOf(sumOf(::playPart1), sumOf(::playPart2))
}

private enum class RPSResult(val score: Int, val token: String) { Lose(0, "X"), Draw(3, "Y"), Win(6, "Z") }

private enum class RPSMove(val score: Int, vararg val tokens: String) {
    Rock(1, "A", "X"), Paper(2, "B", "Y"), Scissors(3, "C", "Z");

    fun playAgainst(move: RPSMove): RPSResult = when {
        this == beats(move) -> Lose
        this == move -> Draw
        else -> Win
    }

    private fun beats(move: RPSMove) = when (move) {
        Paper -> Rock
        Rock -> Scissors
        Scissors -> Paper
    }

    fun whatToPlayToAchieve(result: RPSResult) = RPSMove.values().first { it.playAgainst(this) == result }
}