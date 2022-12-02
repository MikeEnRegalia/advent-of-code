package aoc2022

import aoc2022.RPSMove.*
import aoc2022.RPSResult.*

fun main() = day02Clean(String(System.`in`.readAllBytes())).forEach(::println)

fun day02Clean(input: String) = with(input.lines().map { it.split(" ") }) {
    listOf(sumOf(::playPart1), sumOf(::playPart2))
}

private fun playPart1(line: List<String>): Int {
    val (opponentMove, myMove) = line.map { it.toRPSMove() }
    val result = myMove.playAgainst(opponentMove)
    return myMove.score + result.score
}

private fun playPart2(line: List<String>): Int {
    val opponentMove = line[0].toRPSMove()
    val desiredResult = line[1].toRPSResult()
    val myMove = opponentMove.whatToPlayToAchieve(desiredResult)
    return myMove.score + desiredResult.score
}

private enum class RPSMove(val score: Int) { Rock(1), Paper(2), Scissors(3) }

private fun beats(move: RPSMove) = when (move) {
    Paper -> Rock
    Rock -> Scissors
    Scissors -> Paper
}

private fun RPSMove.playAgainst(move: RPSMove): RPSResult = when {
    this == beats(move) -> Lose
    this == move -> Draw
    else -> Win
}

private fun RPSMove.whatToPlayToAchieve(result: RPSResult) =
    RPSMove.values().first { it.playAgainst(this) == result }

private fun String.toRPSMove() = when (this) {
    "A", "X" -> Rock
    "B", "Y" -> Paper
    "C", "Z" -> Scissors
    else -> throw IllegalArgumentException(this)
}

private enum class RPSResult(val score: Int) { Win(6), Lose(0), Draw(3) }

private fun String.toRPSResult() = when (this) {
    "X" -> Lose
    "Y" -> Draw
    "Z" -> Win
    else -> throw IllegalArgumentException(this)
}