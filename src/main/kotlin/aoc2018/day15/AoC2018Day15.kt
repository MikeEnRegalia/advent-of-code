package aoc2018.day15

import aoc2018.day15.Faction.ELF
import aoc2018.day15.Faction.GOBLIN

internal typealias Dungeon = Map<Pos, Tile>
internal typealias MutableDungeon = MutableMap<Pos, Tile>

internal data class Score(val elvesDied: Int, val outcome: Int)

fun day15BeverageBanditsPart2(input: String): Int = toInfinity()
    .map { power -> input.toDungeon(power).beverageBandits() }
    .filter { it.elvesDied == 0 }
    .first().outcome

fun day15BeverageBanditsPart1(input: String): Int = input.toDungeon(3).beverageBandits().outcome

internal fun MutableDungeon.beverageBandits(): Score {
    val initialElves = values.count { it.isElf() }
    return toInfinity().mapNotNull { round -> fight(initialElves, round) }.first()
}

internal fun toInfinity() = sequence { var i = 0; while (true) yield(i++) }

internal fun MutableDungeon.fight(totalElves: Int, round: Int): Score? {
    for ((fighterPos, fighter) in fighters()) {
        if (this[fighterPos] is Space) continue
        fighter as Fighter

        val adjacentTarget = adjacentTarget(fighterPos)
        if (adjacentTarget != null) {
            attack(fighter, adjacentTarget)
            continue
        }

        val targets = targets(fighter) ?: return score(totalElves, round)

        val nextPos = move(fighterPos, targets) ?: continue

        this[fighterPos] = Space
        this[nextPos] = fighter
        adjacentTarget(nextPos)?.let { attack(fighter, it) }
    }
    return null
}

private fun Tile.isElf() = this is Fighter && faction == ELF

private fun Dungeon.score(initialElves: Int, round: Int): Score {
    val elvesDied = initialElves - values.count { it.isElf() }
    val health = values.sumOf { if (it is Fighter) it.health else 0 }
    return Score(elvesDied, round * health)
}

internal fun String.toDungeon(elvesAttackPower: Int) = split("\n").mapIndexed { y, row ->
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