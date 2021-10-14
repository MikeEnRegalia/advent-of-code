package aoc2018

fun day15BeverageBandits(input: String): Pair<Int, Int> {
    val map = input.toMap()
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

internal fun Map<Pos, Tile>.reachableFrom(start: Pos): Map<Pos, Int> {
    val result = mutableMapOf<Pos, Int>()
    fun follow(pos: Pos, travelled: Int = 0) {
        val curr = result[pos]
        if (curr != null && travelled > curr) return
        result[pos] = travelled
        pos.neighbors().filter {
            val itKnownDistance = result[it]
            isSpace(it) && (itKnownDistance == null || itKnownDistance > travelled + 1)
        }.forEach { follow(it, travelled + 1) }
    }
    follow(start)
    return result
}

internal fun Map<Pos, Tile>.debug(fighterAt: Pos? = null): String = with(keys) {
    minToMaxOf { it.y }.joinToString("\n") { y ->
        minToMaxOf { it.x }.joinToString("") { x ->
            val pos = Pos(x, y)
            if (isSpace(pos)) "."
            else if (isWall(pos)) "#"
            else if (get(pos)!! is Elf) if (fighterAt != null && fighterAt == pos) "e" else "E"
            else if (get(pos)!! is Goblin) if (fighterAt != null && fighterAt == pos) "g" else "G"
            else throw IllegalStateException()
        }
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
internal object Wall : Tile()
internal object Space : Tile()

internal abstract class Fighter(val health: Int, val attackPower: Int) : Tile() {
    abstract fun hitBy(fighter: Fighter): Fighter
}

internal class Elf(health: Int, attackPower: Int) : Fighter(health, attackPower) {
    override fun hitBy(fighter: Fighter) = Elf(health - fighter.attackPower, attackPower)
}

internal class Goblin(health: Int, attackPower: Int) : Fighter(health, attackPower) {
    override fun hitBy(fighter: Fighter) = Goblin(health - fighter.attackPower, attackPower)
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
internal fun Map<Pos, Tile>.isWall(pos: Pos) = tile(pos) is Wall

internal fun String.toMap() = split("\n")
    .mapIndexed { y, row ->
        row.mapIndexed { x, c ->
            when (c) {
                '#' -> Wall
                'E' -> Elf(200, 3)
                'G' -> Goblin(200, 3)
                '.' -> Space
                else -> throw IllegalArgumentException(c.toString())
            }.let { Pos(x, y) to it }
        }
    }.flatten().toMap().toMutableMap()