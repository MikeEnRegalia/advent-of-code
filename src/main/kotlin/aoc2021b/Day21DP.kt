package aoc2021b

import kotlin.math.max
import kotlin.math.min

fun main(vararg args: String) {
    val start = args.map { it.toInt() - 1 }.let { State(player1 = Player(it[0]), player2 = Player(it[1])) }
    println(start.part1())
    println(start.part2())
}

data class Player(val pos: Int, val score: Int = 0) {
    fun move(castDie: Int) = ((pos + castDie) % 10).let { newPos1 -> Player(newPos1, score + newPos1 + 1) }
}

data class State(val player1: Player, val player2: Player, val turn: Int = 0) {
    val doesInitialPlayer1Lead = turn % 2 == if (player1.score >= player2.score) 1 else 0
    fun isGameOver(minScore: Int) = max(player1.score, player2.score) >= minScore
    fun nextTurn(castDie: Int) = State(player2, player1.move(castDie), turn + 1)
}

fun State.part1(): Int {
    val die = generateSequence(1) { if (it == 100) 1 else it + 1 }.chunked(3, List<Int>::sum).iterator()

    fun State.play(): Int = when {
        isGameOver(minScore = 1000) -> min(player1.score, player2.score) * turn * 3
        else -> nextTurn(die.next()).play()
    }
    return play()
}

data class Score(val won: Long = 0, val lost: Long = 0) {
    fun plus(score: Score) = Score(won + score.won, lost + score.lost)
}

fun State.part2(): Long {
    val dieCasts = listOf(1, 2, 3).run { flatMap { a -> flatMap { b -> map { c -> a + b + c } } } }

    val cache = mutableMapOf<State, Score>()
    fun State.play(): Score = when {
        isGameOver(minScore = 21) -> if (doesInitialPlayer1Lead) Score(won = 1) else Score(lost = 1)
        else -> cache.getOrPut(this) { dieCasts.map(::nextTurn).map(State::play).reduce(Score::plus) }
    }
    return play().run { max(won, lost) }
}