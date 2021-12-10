package aoc2015.day21

import kotlin.math.max

fun main() {
    val boss = Stats(100, listOf(Item(0, 8, 2)))
    equipments()
        .partition { play(it, boss) }
        .let { (won, lost) ->
            won.minOf { it.cost }.also { println(it) }
            lost.maxOf { it.cost }.also { println(it) }
        }
}

fun play(player: Stats, boss: Stats): Boolean {
    sequence {
        while (true) {
            yield(0 to 1)
            yield(1 to 0)
        }
    }.fold(mutableListOf(player, boss)) { players, (player, opponent) ->
        players[opponent] = players[opponent].hitBy(players[player])
        if (players[opponent].hitPoints <= 0) return opponent == 1
        players
    }
    return false
}

data class Item(val cost: Int, val damage: Int, val armor: Int)

data class Stats(val hitPoints: Int, val items: List<Item>) {
    val cost = items.sumOf { it.cost }
    val damage = items.sumOf { it.damage }
    val armor = items.sumOf { it.armor }
    fun hitBy(o: Stats) = copy(hitPoints = hitPoints - damageDoneBy(o))
    fun with(i: Item) = copy(items = items + i)
}

fun Stats.damageDoneBy(o: Stats) = max(1, o.damage - armor)

private fun equipments() = sequence {
    fun weapons(vararg data: Pair<Int, Int>) = data.map { (cost, dmg) -> Item(cost, dmg, 0) }
    fun armors(vararg data: Pair<Int, Int>) = data.map { Item(it.first, 0, it.second) }

    val weapons = weapons(8 to 4, 10 to 5, 25 to 6, 40 to 7, 74 to 8)
    val armor = armors(13 to 1, 31 to 2, 53 to 3, 75 to 4, 102 to 5)
    val rings = weapons(25 to 1, 50 to 2, 100 to 3) + armors(20 to 1, 40 to 2, 80 to 3)

    fun equip(vararg items: Item) = Stats(100, items.toList())

    for (w in weapons) {
        yield(equip(w))
        for (a in armor) {
            yield(equip(w, a))
            for (r in rings) {
                yield(equip(w, r))
                yield(equip(w, a, r))
                for (r2 in rings.filterNot { it == r }) {
                    yield(equip(w, r, r2))
                    yield(equip(w, a, r, r2))
                }
            }
        }
    }
}