package aoc2018

fun day15BeverageBanditsPart2(input: String): Int {
    var attackPower = 4
    while (true) {
        val (elvesLost, outcome) = beverageBandits(input, elvesAttackPower = attackPower++)
        if (elvesLost == 0) return outcome
    }
}

fun day15BeverageBanditsPart1(input: String): Int =
    beverageBandits(input).second

internal fun beverageBandits(input: String, elvesAttackPower: Int = 3): Pair<Int, Int> {
    val map = input.toMap(elvesAttackPower)
    val totalElves = map.values.count { it is Elf }

    var round = 0
    while (true) {
        with(map) {
            fighters().forEach { (fighterPos, fighter) ->
                if (this[fighterPos] is Space) return@forEach
                fighter as Fighter

                val adjacentTarget = adjacentTarget(fighterPos, fighter)
                if (adjacentTarget != null) {
                    attack(fighter, adjacentTarget)
                } else {
                    val targets = targets(fighter)
                        .takeIf { it.isNotEmpty() }
                        ?: return wrapResult(totalElves, round)

                    val nextPos = move(fighterPos, targets)
                    if (nextPos != null) {
                        this[fighterPos] = Space
                        this[nextPos] = fighter
                        adjacentTarget(nextPos, fighter)?.let { attack(fighter, it) }
                    }
                }
            }
        }
        round++
    }
}

private fun Map<Pos, Tile>.wrapResult(totalElves: Int, round: Int): Pair<Int, Int> {
    val remainingElves = values.count { it is Elf }
    val elvesLost = totalElves - remainingElves
    val health = values.sumOf { if (it is Fighter) it.health else 0 }
    return elvesLost to round * health
}

internal fun String.toMap(elvesAttackPower: Int) = split("\n")
    .mapIndexed { y, row ->
        row.mapIndexed { x, c -> Pos(x, y) to c.toTile(elvesAttackPower) }
    }.flatten().toMap().toMutableMap()

private fun Char.toTile(elvesAttackPower: Int) = when (this) {
    '#' -> Wall
    'E' -> Elf(200, elvesAttackPower)
    'G' -> Goblin(200, 3)
    '.' -> Space
    else -> throw IllegalArgumentException(toString())
}

internal fun Map<Pos, Tile>.reachableFrom(start: Pos): Map<Pos, Int> = mutableMapOf<Pos, Int>().also { result ->
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
internal fun Map<Pos, Tile>.debug() = with(keys) {
    minToMaxOf { it.y }.joinToString("\n") { y ->
        minToMaxOf { it.x }.joinToString("") { x -> get(Pos(x, y))!!.toString() }
    }
}

internal inline fun Set<Pos>.minToMaxOf(f: (Pos) -> Int) = minOf(f)..maxOf(f)

internal fun MutableMap<Pos, Tile>.move(pos: Pos, targets: List<Pos>): Pos? {
    val allInRange = filter { (key, value) -> value is Space && targets.anyAdjacentTo(key) }.keys
        .takeIf { it.isNotEmpty() } ?: return null

    val allReachable = reachableFrom(pos)
    val reachable = allInRange.mapNotNull { allReachable[it]?.let { distance -> it to distance } }
        .takeIf { it.isNotEmpty() } ?: return null

    val inRangePos = reachable.filterByMinOf { it.second }.map { it.first }.minOf { it }

    return reachableFrom(inRangePos)
        .filterKeys { pos.neighbors().contains(it) }
        .entries
        .filterByMinOf { it.value }
        .map { it.key }
        .minOf { it }
}

private fun List<Pos>.anyAdjacentTo(it: Pos) = any { target -> it.adjacentTo(target) }

internal fun Map<Pos, Tile>.adjacentTarget(pos: Pos, tile: Tile) =
    filterKeys { it.adjacentTo(pos) }.targets(tile).filterByMinOf { (this[it] as Fighter).health }.firstOrNull()

internal fun <T> Collection<T>.filterByMinOf(t: (T) -> Int) =
    if (isEmpty()) this else minOf { t(it) }.let { min -> filter { t(it) == min } }

internal fun MutableMap<Pos, Tile>.attack(attacker: Fighter, victim: Pos) {
    this[victim] = (this[victim] as Fighter).hitBy(attacker).let { if (it.health <= 0) Space else it }
}

internal fun Map<Pos, Tile>.fighters() = filterValues { it is Fighter }.entries.sortedBy { it.key }

internal fun Map<Pos, Tile>.targets(tile: Tile) =
    filterValues(tile::isOpponent).keys.sorted()

internal fun Tile.isOpponent(tile: Tile) = if (this is Elf) (tile is Goblin) else (tile is Elf)

internal sealed class Tile

internal object Wall : Tile() {
    override fun toString() = "W"
}

internal object Space : Tile() {
    override fun toString() = "."
}

internal abstract class Fighter(val health: Int, val attackPower: Int) : Tile() {
    abstract fun hitBy(fighter: Fighter): Fighter
}

internal class Elf(health: Int, attackPower: Int) : Fighter(health, attackPower) {
    override fun hitBy(fighter: Fighter) = Elf(health - fighter.attackPower, attackPower)
    override fun toString() = "E"
}

internal class Goblin(health: Int, attackPower: Int) : Fighter(health, attackPower) {
    override fun hitBy(fighter: Fighter) = Goblin(health - fighter.attackPower, attackPower)
    override fun toString() = "G"
}

internal data class Pos(val x: Int, val y: Int) : Comparable<Pos> {
    fun neighbors() = sequenceOf(copy(y = y - 1), copy(x = x - 1), copy(x = x + 1), copy(y = y + 1))

    fun adjacentTo(target: Pos) = target.neighbors().contains(this)

    override fun toString() = "($x,$y)"

    override fun compareTo(other: Pos) = when {
        this == other -> 0
        y - other.y != 0 -> y - other.y
        else -> x - other.x
    }
}

internal fun Map<Pos, Tile>.tile(pos: Pos) = this[pos] ?: Space
internal fun Map<Pos, Tile>.isSpace(pos: Pos) = tile(pos) is Space