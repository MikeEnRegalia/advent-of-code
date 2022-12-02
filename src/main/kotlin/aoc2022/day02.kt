package aoc2022

import aoc2022.RPSMove.*
import aoc2022.RPSResult.*

fun main() = day02Clean(String(System.`in`.readAllBytes())).forEach(::println)

fun day02KISS(input: String) = with(input.lines()) { listOf(sumOf(::part1Round), sumOf(::part2Round)) }

private fun part1Round(line: String) = when (line) {
    "A X" -> 1 + 3
    "B X" -> 1 + 0
    "C X" -> 1 + 6
    "A Y" -> 2 + 6
    "B Y" -> 2 + 3
    "C Y" -> 2 + 0
    "A Z" -> 3 + 0
    "B Z" -> 3 + 6
    "C Z" -> 3 + 3
    else -> throw IllegalArgumentException(line)
}

private fun part2Round(line: String) = when (line) {
    "A X" -> 3 + 0
    "B X" -> 1 + 0
    "C X" -> 2 + 0
    "A Y" -> 1 + 3
    "B Y" -> 2 + 3
    "C Y" -> 3 + 3
    "A Z" -> 2 + 6
    "B Z" -> 3 + 6
    "C Z" -> 1 + 6
    else -> throw IllegalArgumentException(line)
}

fun day02Clean(input: String) = with(input.lines().map { it.split(" ") }) {
    listOf(sumOf(::playPart1), sumOf(::playPart2))
}

private fun playPart1(line: List<String>): Int {
    val (opponentMove, myMove) = line.map { it.toRPSMove() }
    return myMove.score + myMove.playAgainst(opponentMove).score
}

private fun playPart2(line: List<String>): Int {
    val opponentMove = line[0].toRPSMove()
    val desiredResult = line[1].toRPSResult()
    val myMove = opponentMove.whatToPlayToAchieve(desiredResult)
    return myMove.score + myMove.playAgainst(opponentMove).score
}

private enum class RPSMove(val score: Int) { Rock(1), Paper(2), Scissors(3) }

private fun RPSMove.playAgainst(move: RPSMove): RPSResult = when (this to move) {
    Rock to Scissors, Paper to Rock, Scissors to Paper -> Win
    Scissors to Rock, Paper to Scissors, Rock to Paper -> Lose
    else -> Draw
}
private fun RPSMove.whatToPlayToAchieve(result: RPSResult) = when (result to this) {
    Win to Rock, Lose to Scissors -> Paper
    Win to Paper, Lose to Rock -> Scissors
    Win to Scissors, Lose to Paper -> Rock
    else -> this
}

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


