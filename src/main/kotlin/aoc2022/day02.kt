package aoc2022

import aoc2022.RPSResult.*
import aoc2022.RPSResult.values as RPSResults

fun main() = day02Clean(String(System.`in`.readAllBytes())).forEach(::println)

fun day02Clean(input: String) = with(input.lines().map { it.split(" ") }) {
    fun playPart1(line: List<String>): Int {
        val (opponentMove, myMove) = line.map(String::toRPSMove)
        val result = myMove.playAgainst(opponentMove)
        return myMove.score + result.score
    }

    fun playPart2(line: List<String>): Int {
        val opponentMove = line[0].toRPSMove()
        val desiredResult = line[1].toRPSResult()
        val myMove = opponentMove.whatAchieves(desiredResult)
        return myMove.score + desiredResult.score
    }
    listOf(sumOf(::playPart1), sumOf(::playPart2))
}

private enum class RPSResult(val score: Int, val token: String) { Lose(0, "X"), Draw(3, "Y"), Win(6, "Z") }

private fun String.toRPSResult() = RPSResults().single { it.token == this }

private enum class RPSMove(val score: Int, vararg val tokens: String) {
    Rock(1, "A", "X"), Paper(2, "B", "Y"), Scissors(3, "C", "Z");

    fun playAgainst(opponentMove: RPSMove): RPSResult = when {
        this == defeatedBy(opponentMove) -> Lose
        this == opponentMove -> Draw
        else -> Win
    }

    private fun defeatedBy(opponentMove: RPSMove) = when (opponentMove) {
        Paper -> Rock
        Rock -> Scissors
        Scissors -> Paper
    }

    fun whatAchieves(result: RPSResult) = RPSMove.values().first { it.playAgainst(this) == result }
}

private fun String.toRPSMove() = RPSMove.values().single { this in it.tokens }