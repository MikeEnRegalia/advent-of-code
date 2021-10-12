package aoc2018

import kotlin.math.abs

fun day15BeverageBandits(input: String): Int {
    val map = input.toMap().toMutableMap()
    var round = 0
    while (true) {
        map.debug().also { println(it) }
        with(map) {
            fighters().forEach { (pos, tile) ->
                if (this[pos] is Space) return@forEach
                if (adjacentTarget(pos, tile)?.let { attack(it) } == null) {
                    val targets = targets(tile)
                    if (targets.isEmpty()) {
                        val elvesSum = map.filterValues { it is Elf }
                            .entries.sumOf { (it.value as Elf).health }
                        println("done after $round complete rounds with $elvesSum remaining elves health.")
                        return round * elvesSum
                    }
                    move(pos, tile, targets)?.let { nextPos ->
                        adjacentTarget(nextPos, tile)?.let { attack(it) }
                    }
                }
            }
        }
        round++
    }
}

internal fun Map<Pos, Tile>.debug(): String =
    (keys.minOf { it.y }..keys.maxOf { it.y }).joinToString("\n") { y ->
        (keys.minOf { it.x }..keys.maxOf { it.x }).joinToString("") { x ->
            when (this[Pos(x, y)]!!) {
                is Space -> "."
                is Wall -> "#"
                is Elf -> "E"
                is Goblin -> "G"
                else -> throw IllegalStateException()
            }
        }
    }

internal fun MutableMap<Pos, Tile>.move(pos: Pos, tile: Tile, targets: List<Pos>): Pos? {

    val inRange = filter { it.value is Space && targets.any { target -> it.key.adjacentTo(target) } }
        .keys.mapNotNull { shortestPath(listOf(pos), it)?.let { path -> it to path.drop(1) } }
    if (inRange.isEmpty()) {
        println("no targets reachable for $pos")
        return null
    }

    val shortestInRange = inRange.filterByMin { it.second.size }
    val next = if (shortestInRange.size == 1) shortestInRange.first().second.first() else {
        shortestInRange.first { it.first == shortestInRange.map { it.first }.toSet().byReadingOrder().first() }
            .second.first()
    }
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

internal class PathFinderOptimizer {
    var minLength: Int? = null
    fun registerLength(l: Int) {
        minLength.let {
            if (it != null && l > it) minLength = l
        }
    }

    fun needLongerThan(l: Int) = minLength.let { it == null || l < it }
}

internal fun Map<Pos, Tile>.shortestPath(
    path: List<Pos>,
    to: Pos,
    optimizer: PathFinderOptimizer = PathFinderOptimizer()
): List<Pos>? =
    if (!optimizer.needLongerThan(path.size)) null
    else if (path.last() == to) path
    else if (path.last().adjacentTo(to)) path.plus(to)
    else path.last().neighbors().filter { this[it] is Space && !path.contains(it) }
        .sortedBy { it.distanceTo(to) }
        .mapNotNull { shortestPath(path.plus(it), to, optimizer) }
        .minByOrNull { it.size }?.also {
            optimizer.registerLength(it.size)
        }

internal fun MutableMap<Pos, Tile>.attack(pos: Pos) {
    this[pos] = this[pos]!!.hit().let { if (it is Fighter && it.health <= 0) Space else it }
        .also { println("attacked: $pos -> ${if (it is Fighter) it.health else 0}") }
}

internal fun Map<Pos, Tile>.fighters() =
    filterValues { (it is Elf) || (it is Goblin) }
        .entries.map { it.key to it.value }.byReadingOrder()

internal fun Map<Pos, Tile>.targets(tile: Tile) =
    filterValues { if (tile is Elf) (it is Goblin) else (it is Elf) }
        .keys.byReadingOrder()

internal fun Set<Pos>.byReadingOrder() = sortedWith(compareBy({ it.y }, { it.x }))
internal fun List<Pair<Pos, Tile>>.byReadingOrder() = sortedWith(compareBy({ it.first.y }, { it.first.x }))

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


internal data class Pos(val x: Int, val y: Int) {
    fun adjacentTo(target: Pos) = with(Pos(target.x - x, target.y - y)) {
        x == 0 && (y == -1 || y == 1) || y == 0 && (x == -1 || x == 1)
    }

    fun neighbors() = listOf(Pos(x - 1, y), Pos(x + 1, y), Pos(x, y - 1), Pos(x, y + 1))
    override fun toString() = "($x,$y)"

    fun distanceTo(pos: Pos) = abs(x - pos.x) + abs(y - pos.y)
}

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
    }.flatten().toMap()