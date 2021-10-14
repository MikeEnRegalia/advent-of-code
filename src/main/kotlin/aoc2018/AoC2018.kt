package aoc2018

import aoc2018.Faction.ELF
import aoc2018.Faction.GOBLIN

internal typealias Dungeon = Map<Pos, Tile>
internal typealias MutableDungeon = MutableMap<Pos, Tile>

fun day15BeverageBanditsPart2(input: String): Int {
    var attackPower = 4
    while (true) {
        val (elvesLost, outcome) = beverageBandits(input, elvesAttackPower = attackPower++)
        if (elvesLost == 0) return outcome
    }
}

fun day15BeverageBanditsPart1(input: String): Int = beverageBandits(input).second

internal fun beverageBandits(input: String, elvesAttackPower: Int = 3): Pair<Int, Int> {
    val map = input.toMap(elvesAttackPower)
    val totalElves = map.values.count { it.isElf() }

    var round = 0
    while (true) {
        with(map) {
            fighters().forEach { (fighterPos, fighter) ->
                if (this[fighterPos] is Space) return@forEach
                fighter as Fighter

                val adjacentTarget = adjacentTarget(fighterPos)
                if (adjacentTarget != null) {
                    attack(fighter, adjacentTarget)
                } else {
                    val targets = targets(fighter) ?: return wrapResult(totalElves, round)

                    val nextPos = move(fighterPos, targets)
                    if (nextPos != null) {
                        this[fighterPos] = Space
                        this[nextPos] = fighter
                        adjacentTarget(nextPos)?.let { attack(fighter, it) }
                    }
                }
            }
        }
        round++
    }
}

private fun Tile.isElf() = this is Fighter && faction == ELF

private fun Dungeon.wrapResult(totalElves: Int, round: Int): Pair<Int, Int> {
    val remainingElves = values.count { it.isElf() }
    val elvesLost = totalElves - remainingElves
    val health = values.sumOf { if (it is Fighter) it.health else 0 }
    return elvesLost to round * health
}

internal fun String.toMap(elvesAttackPower: Int) = split("\n").mapIndexed { y, row ->
    row.mapIndexed { x, c -> Pos(x, y) to c.toTile(elvesAttackPower) }
}.flatten().toMap().toMutableMap()

private fun Char.toTile(elvesAttackPower: Int) = when (this) {
    '#' -> Wall
    '.' -> Space
    'E' -> Fighter(ELF, 200, elvesAttackPower)
    'G' -> Fighter(GOBLIN, 200, 3)
    else -> throw IllegalArgumentException(toString())
}

internal fun Dungeon.distancesFrom(start: Pos): Map<Pos, Int> = mutableMapOf<Pos, Int>().also { result ->
    fun follow(pos: Pos, travelled: Int = 0) {
        result[pos] = travelled
        pos.neighbors()
            .filter { isSpace(it) }
            .filter { neighbor -> result[neighbor]?.let { it > travelled + 1 } ?: true }
            .forEach { follow(it, travelled + 1) }
    }
    follow(start)
}

@Suppress("unused")
internal fun Dungeon.debug() = with(keys) {
    minToMaxOf { it.y }.joinToString("\n") { y ->
        minToMaxOf { it.x }.joinToString("") { x -> get(Pos(x, y))!!.toString() }
    }
}

internal inline fun Set<Pos>.minToMaxOf(f: (Pos) -> Int) = minOf(f)..maxOf(f)

internal data class RemotePosition(val pos: Pos, val distance: Int)

internal fun Dungeon.move(pos: Pos, targets: List<Pos>): Pos? {
    val allInRange = allInRangeOf(targets) ?: return null
    val reachable = onlyReachable(pos, allInRange) ?: return null
    return neighborsToward(reachable.closest(), from = pos).closest()
}

internal fun Dungeon.neighborsToward(closest: Pos, from: Pos) =
    distancesFrom(closest)
        .filterKeys { from.neighbors().contains(it) }
        .map { RemotePosition(it.key, it.value) }

internal fun Dungeon.allInRangeOf(targets: List<Pos>) =
    filter { (key, value) -> value is Space && targets.anyAdjacentTo(key) }.keys.takeIf { it.isNotEmpty() }

internal fun Dungeon.onlyReachable(pos: Pos, positions: Set<Pos>): List<RemotePosition>? {
    val allReachable = distancesFrom(pos)
    return positions.mapNotNull { allReachable[it]?.let { distance -> RemotePosition(it, distance) } }
        .takeIf { it.isNotEmpty() }
}

internal fun List<RemotePosition>.closest() = filterByMinOf { it.distance }.map { it.pos }.minOf { it }

internal fun List<Pos>.anyAdjacentTo(pos: Pos) = any { it.neighbors().contains(pos) }

internal fun Dungeon.adjacentTarget(pos: Pos) =
    adjacentTargets(pos)
        ?.filterByMinOf { (this[it] as Fighter).health }
        ?.firstOrNull()

internal fun Dungeon.adjacentTargets(pos: Pos) =
    filterKeys { it.neighbors().contains(pos) }.targets(this[pos] as Fighter)

internal fun <T> Collection<T>.filterByMinOf(t: (T) -> Int) = minOf { t(it) }.let { min -> filter { t(it) == min } }

internal fun MutableDungeon.attack(attacker: Fighter, victim: Pos) {
    this[victim] = (this[victim] as Fighter).hitBy(attacker).let { if (it.health <= 0) Space else it }
}

internal fun Dungeon.fighters() = filterValues { it is Fighter }.entries.sortedBy { it.key }

internal fun Dungeon.targets(tile: Fighter) = filterValues(tile::isOpponent).keys.sorted().takeIf { it.isNotEmpty() }

internal fun Fighter.isOpponent(tile: Tile) = when (faction) {
    ELF -> tile is Fighter && tile.faction == GOBLIN
    GOBLIN -> tile is Fighter && tile.faction == ELF
}

internal sealed class Tile

internal object Wall : Tile() {
    override fun toString() = "W"
}

internal object Space : Tile() {
    override fun toString() = "."
}

internal enum class Faction {
    ELF,
    GOBLIN
}

internal data class Fighter(val faction: Faction, val health: Int, val attackPower: Int) : Tile() {
    fun hitBy(fighter: Fighter) = copy(health = health - fighter.attackPower)
    override fun toString() = if (faction == ELF) "E" else "G"
}

internal data class Pos(val x: Int, val y: Int) : Comparable<Pos> {
    fun neighbors() = sequenceOf(copy(y = y - 1), copy(x = x - 1), copy(x = x + 1), copy(y = y + 1))

    override fun toString() = "($x,$y)"

    override fun compareTo(other: Pos) = when {
        this == other -> 0
        y - other.y != 0 -> y - other.y
        else -> x - other.x
    }
}

internal fun Dungeon.tile(pos: Pos) = this[pos] ?: Space
internal fun Dungeon.isSpace(pos: Pos) = tile(pos) is Space