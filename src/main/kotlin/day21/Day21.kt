package day21

import kotlin.math.max

data class Item(val cost: Int, val damage: Int, val armor: Int)
data class Stats(val hitPoints: Int, val items: List<Item>) {
    val cost = items.sumOf { it.cost }
    val damage = items.sumOf { it.damage }
    val armor = items.sumOf { it.armor }
    fun hitBy(opponent: Stats) = copy(hitPoints = this.hitPoints - max(1, opponent.damage - this.armor))
}

val weapons = listOf(
    Item(8, 4, 0),
    Item(10, 5, 0),
    Item(25, 6, 0),
    Item(40, 7, 0),
    Item(74, 8, 0),
)

val armor = listOf(
    Item(13, 0, 1),
    Item(31, 0, 2),
    Item(53, 0, 3),
    Item(75, 0, 4),
    Item(102, 0, 5)
)

val rings = listOf(
    Item(25, 1, 0),
    Item(50, 2, 0),
    Item(100, 3, 0),
    Item(20, 0, 1),
    Item(40, 0, 2),
    Item(80, 0, 3)
)

fun main() {
    val boss = Stats(100, listOf(Item(0, 8, 2)))

    allPlayers().filter { play(it, boss) }.minOf { it.cost }.also { println(it) }
    allPlayers().filter { !play(it, boss) }.maxOf { it.cost }.also { println(it) }
}

private fun allPlayers() = sequence {
    for (w in weapons) {
        yield(Stats(100, listOf(w)))
        for (a in armor) {
            yield(Stats(100, listOf(w, a)))
            for (r1 in rings) {
                yield(Stats(100, listOf(w, r1)))
                yield(Stats(100, listOf(w, a, r1)))
                for (r2 in rings) {
                    if (r1 != r2) {
                        yield(Stats(100, listOf(w, r1, r2)))
                        yield(Stats(100, listOf(w, a, r1, r2)))
                    }
                }
            }
        }
    }
}

fun play(player: Stats, boss: Stats): Boolean {
    val moves = sequence {
        while (true) {
            yield(IndexedValue(0, 1))
            yield(IndexedValue(1, 0))
        }
    }
    val players = mutableListOf(player, boss)
    for ((move, nextMove) in moves) {
        players[nextMove].hitBy(players[move]).also {
            players[nextMove] = it
            if (it.hitPoints <= 0) return move == 0
        }
    }
    return false
}

