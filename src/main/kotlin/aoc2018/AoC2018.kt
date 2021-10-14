package aoc2018

import kotlin.math.abs

fun day15BeverageBandits(input: String): Int {
    val map = input.toMap()

    map.debug().also { println(it) }

    var round = 0
    while (true) {
        with(map) {
            fighters().forEach { (pos, tile) ->
                if (this[pos] is Space) return@forEach
                if (adjacentTarget(pos, tile)?.let { attack(it) } == null) {
                    val targets = targets(tile)
                    if (targets.isEmpty()) {
                        val health = map.values.sumOf { if (it is Fighter) it.health else 0 }
                        println("done after $round complete rounds with $health remaining health.")
                        return round * health
                    }
                    move(pos, tile, targets)
                        ?.let { adjacentTarget(it, tile) }
                        ?.let { attack(it) }
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
            val curr = result[it]
            isSpace(it) && (curr == null || curr > travelled + 1)
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
    println("map for ${fighter::class.simpleName} at $pos:\n${debug()}")
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
    println("moved from $pos to $next")
    return next
}

internal fun Map<Pos, Tile>.adjacentTarget(pos: Pos, tile: Tile) =
    filterKeys { it.adjacentTo(pos) }.targets(tile).filterByMin { (this[it] as Fighter).health }.firstOrNull()

internal fun <T> Collection<T>.filterByMin(f: (T) -> Int): Collection<T> {
    if (isEmpty()) return this
    val min = minOf { f(it) }
    return filter { f(it) == min }
}

internal fun MutableMap<Pos, Tile>.attack(pos: Pos) {
    this[pos] =
        this[pos]!!.hit().let {
            if (it is Fighter && it.health <= 0) Space else it
        }
}

internal fun Map<Pos, Tile>.fighters() =
    filterValues { it is Fighter }.entries.map { it.key to it.value }.sortedBy { it.first }

internal fun Map<Pos, Tile>.targets(tile: Tile) =
    filterValues { if (tile is Elf) (it is Goblin) else (it is Elf) }
        .keys.sorted()

internal sealed class Tile {
    abstract fun hit(): Tile
}

internal object Wall : Tile() {
    override fun hit() = Wall
}

internal object Space : Tile() {
    override fun hit() = Space
}

internal abstract class Fighter(val health: Int) : Tile()
internal class Elf(health: Int) : Fighter(health) {
    override fun hit() = Elf(health - 3)
}

internal class Goblin(health: Int) : Fighter(health) {
    override fun hit() = Goblin(health - 3)
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

    fun distanceTo(pos: Pos) = abs(x - pos.x) + abs(y - pos.y)
}

internal fun Map<Pos, Tile>.tile(pos: Pos) = this[pos] ?: Space
internal fun Map<Pos, Tile>.isSpace(pos: Pos) = tile(pos) is Space
internal fun Map<Pos, Tile>.isWall(pos: Pos) = tile(pos) is Wall
internal fun Map<Pos, Tile>.isObstacle(pos: Pos, fighter: Tile, fighterPos: Pos) =
    pos != fighterPos && (isWall(pos) || tile(pos)::class == fighter::class)

internal fun Map<Pos, Tile>.neighborSpaces(pos: Pos) = pos.neighbors().filter { this[it] is Space }

internal fun String.toMap() = split("\n")
    .mapIndexed { y, row ->
        row.mapIndexed { x, c ->
            when (c) {
                '#' -> Wall
                'E' -> Elf(200)
                'G' -> Goblin(200)
                '.' -> Space
                else -> throw IllegalArgumentException(c.toString())
            }.let { Pos(x, y) to it }
        }
    }.flatten().toMap().toMutableMap()