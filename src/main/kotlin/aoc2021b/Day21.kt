package aoc2021b

fun main(vararg args: String) {
    val positions = listOf(args[0].toInt(), args[1].toInt()).map { it - 1 }

    val part1Die = generateSequence(1) { it + 1 }.chunked(3).map { listOf(it.sum()) }.iterator()
    val (part1Game, _) = play(positions, winningScore = 1000, part1Die.iterator())
    with(part1Game) { println(score[turn % 2] * turn * 3) }

    val dirac = listOf(1, 2, 3).run { flatMap { a -> flatMap { b -> map { a + b + it } } } }
    val (_, part2Wins) = play(positions, winningScore = 21, generateSequence { dirac }.iterator())
    with(part2Wins) { println(maxOf { it }) }
}

data class Game(val pos: MutableList<Int>, val score: MutableList<Int> = mutableListOf(0, 0), var turn: Int = 0) {
    fun next(cast: Int) = copy(pos = pos.toMutableList(), score = score.toMutableList()).apply {
        val player = turn % 2
        pos[player] = (pos[player] + cast) % 10
        score[player] += pos[player] + 1
        turn++
    }
}

fun play(start: List<Int>, winningScore: Int, dice: Iterator<List<Int>>): Pair<Game, List<Long>> {
    val statistics = mutableListOf(0L, 0L)
    var games = mapOf(Game(start.toMutableList()) to 1L)
    var firstGameOver: Game? = null
    while (games.isNotEmpty()) {
        games = buildMap {
            games.forEach { (game, n) ->
                dice.next().map { game.next(it) to game.turn % 2 }.forEach { (game, player) ->
                    if (game.score[player] < winningScore) compute(game) { _, prev -> (prev ?: 0L) + n }
                    else {
                        statistics[player] = statistics[player] + n
                        firstGameOver = firstGameOver ?: game
                    }
                } to n
            }
        }
    }
    return firstGameOver!! to statistics
}