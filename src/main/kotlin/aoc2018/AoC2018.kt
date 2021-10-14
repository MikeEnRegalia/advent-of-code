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
    val map = input.loadInput(elvesAttackPower)
    val totalElves = map.values.count { it is Elf }

    var round = 0
    while (true) {
        with(map) {
            fighters().forEach { (pos, fighter) ->
                if (this[pos] is Space) return@forEach
                fighter as Fighter

                if (adjacentTarget(pos, fighter)?.let { attack(it, fighter) } == null) {
                    val targets = targets(fighter)
                    if (targets.isEmpty()) {
                        val remainingElves = map.values.count { it is Elf }
                        val elvesLost = totalElves - remainingElves
                        val health = map.values.sumOf { if (it is Fighter) it.health else 0 }
                        println("done after $round complete rounds with $health remaining health, $elvesLost elves lost.")
                        return elvesLost to round * health
                    }
                    move(pos, fighter, targets)
                        ?.let { adjacentTarget(it, fighter) }
                        ?.let { attack(it, fighter) }
                }
            }
        }
        round++
    }
}

internal fun String.loadInput(elvesAttackPower: Int) = split("\n")
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

internal fun Map<Pos, Tile>.reachableFrom(start: Pos): Map<Pos, Int> {
    val result = mutableMapOf<Pos, Int>()
    fun follow(pos: Pos, travelled: Int = 0) {
        result[pos]?.let { if (it < travelled) return }
        result[pos] = travelled
        pos.neighbors()
            .filter { isSpace(it) }
            .filter { neighbor -> result[neighbor]?.let { it > travelled + 1 } ?: true }
            .forEach { follow(it, travelled + 1) }
    }
    follow(start)
    return result
}

@Suppress("unused")
internal fun Map<Pos, Tile>.debug() = with(keys) {
    minToMaxOf { it.y }.joinToString("\n") { y ->
        minToMaxOf { it.x }.joinToString("") { x -> get(Pos(x, y))!!.toString() }
    }
}

internal inline fun Set<Pos>.minToMaxOf(f: (Pos) -> Int) = minOf(f)..maxOf(f)

internal fun MutableMap<Pos, Tile>.move(pos: Pos, fighter: Tile, targets: List<Pos>): Pos? {
    val inRange = filter { it.value is Space && targets.any { target -> it.key.adjacentTo(target) } }.keys
    if (inRange.isEmpty()) return null

    val reachable = reachableFrom(pos)
    val reachableInRange = inRange.mapNotNull { reachable[it]?.let { distance -> it to distance } }
    if (reachableInRange.isEmpty()) return null

    val shortestInRange = reachableInRange.filterByMin { it.second }
    val destination = shortestInRange
        .first { (shortest) -> shortest == shortestInRange.map { it.first }.toSet().minOf { it } }
        .first

    val next = reachableFrom(destination)
        .filterKeys { pos.neighbors().contains(it) }
        .entries
        .filterByMin { it.value }
        .map { it.key }.minOf { it }

    this[pos] = Space
    this[next] = fighter
    return next
}

internal fun Map<Pos, Tile>.adjacentTarget(pos: Pos, tile: Tile) =
    filterKeys { it.adjacentTo(pos) }.targets(tile).filterByMin { (this[it] as Fighter).health }.firstOrNull()

internal fun <T> Collection<T>.filterByMin(f: (T) -> Int): Collection<T> {
    if (isEmpty()) return this
    val min = minOf { f(it) }
    return filter { f(it) == min }
}

internal fun MutableMap<Pos, Tile>.attack(victim: Pos, attacker: Fighter) {
    this[victim] =
        (this[victim] as Fighter).hitBy(attacker).let {
            if (it.health <= 0) Space else it
        }
}

internal fun Map<Pos, Tile>.fighters() =
    filterValues { it is Fighter }.entries.map { it.key to it.value }.sortedBy { it.first }

internal fun Map<Pos, Tile>.targets(tile: Tile) =
    filterValues { if (tile is Elf) (it is Goblin) else (it is Elf) }
        .keys.sorted()

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
    fun neighbors() = sequenceOf(above(), left(), right(), below())

    fun below() = copy(y = y + 1)
    fun right() = copy(x = x + 1)
    fun left() = copy(x = x - 1)
    fun above() = copy(y = y - 1)

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