package aoc2018

import kotlin.math.abs

fun day15BeverageBandits(input: String): Int? {
    val map = input.toMap()
    var round = 0
    while (true) {
        map.debug().also { println(it) }
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

internal fun Map<Pos, Tile>.debug(): String = with(keys) {
    minToMaxOf { it.y }.joinToString("\n") { y ->
        minToMaxOf { it.x }.joinToString("") { x ->
            when (get(Pos(x, y))!!) {
                is Space -> "."
                is Wall -> "#"
                is Elf -> "E"
                is Goblin -> "G"
                else -> throw IllegalStateException()
            }
        }
    }
}

internal inline fun Set<Pos>.minToMaxOf(f: (Pos) -> Int) = minOf(f)..maxOf(f)

internal fun MutableMap<Pos, Tile>.move(pos: Pos, tile: Tile, targets: List<Pos>): Pos? {
    val inRange = filter { it.value is Space && targets.any { target -> it.key.adjacentTo(target) } }.keys
    if (inRange.isEmpty()) return null

    val reachable = reachableFrom(pos, stopAt = inRange)
    val reachableInRange = inRange.mapNotNull { reachable[it]?.let { path -> it to path } }
    if (reachableInRange.isEmpty()) {
        //println("no targets reachable for $pos: $inRange, ${reachable.keys}")
        return null
    }

    val shortestInRange = reachableInRange.filterByMin { it.second.size }
    val next = shortestInRange
        .first { (shortest) -> shortest == shortestInRange.map { it.first }.toSet().minOf { it } }
        .second
        .first()

    this[pos] = Space
    this[next] = tile
    println("moved from $pos to $next")
    return next
}

internal fun Map<Pos, Tile>.adjacentTarget(pos: Pos, tile: Tile) =
    filterKeys { it.adjacentTo(pos) }.targets(tile).filterByMin { (this[it] as Fighter).health }.firstOrNull()

internal fun <T> List<T>.filterByMin(f: (T) -> Int): List<T> {
    if (isEmpty()) return this
    val min = minOf { f(it) }
    return filter { f(it) == min }
}

internal fun Map<Pos, Tile>.reachableFrom(start: Pos, stopAt: Set<Pos>): Map<Pos, List<Pos>> {
    val result = mutableMapOf<Pos, List<Pos>>()
    fun addNeighborsOf(pos: Pos, fromStartToPos: List<Pos> = listOf()) {
        for (next in neighborSpaces(pos).sortedBy { stopAt.minOf { stop -> it.distanceTo(stop) } }) {
            val fromStartToNext = fromStartToPos.plus(next)
            if (result[next].preferableTo(fromStartToNext)) continue

            val soFar = result.filterKeys { stopAt.contains(it) }.values.minOfOrNull { it.size }
            if (soFar != null && soFar < fromStartToNext.size) continue

            result[next] = fromStartToNext
            if (!stopAt.contains(next))
                addNeighborsOf(next, fromStartToNext)
        }
    }
    addNeighborsOf(start)
    return result
}

internal fun List<Pos>?.preferableTo(other: List<Pos>) =
    this != null && (size < other.size || size == other.size && this[0] < other[0])

internal fun MutableMap<Pos, Tile>.attack(pos: Pos) {
    this[pos] = this[pos]!!.hit().let { if (it is Fighter && it.health <= 0) Space else it }
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
    fun neighbors() = listOf(Pos(x - 1, y), Pos(x + 1, y), Pos(x, y - 1), Pos(x, y + 1))
    fun adjacentTo(target: Pos) = target.neighbors().contains(this)

    override fun toString() = "($x,$y)"

    override fun compareTo(other: Pos) = when {
        this == other -> 0
        y - other.y != 0 -> y - other.y
        else -> x - other.x
    }

    fun distanceTo(pos: Pos) = abs(x - pos.x) + abs(y - pos.y)
}

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