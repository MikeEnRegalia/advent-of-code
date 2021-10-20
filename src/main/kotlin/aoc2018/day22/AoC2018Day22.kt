package aoc2018.day22

typealias Cache = MutableMap<Pair<Int, Int>, Int>

fun day22(depth: Int, targetX: Int, targetY: Int): Int {
    val cache: Cache = mutableMapOf()
    return (0..targetX).flatMap { x ->
        (0..targetY).map { y -> erosionLevel(depth, x, y, targetX, targetY, cache) % 3 }
    }.sum()
}

internal fun geologicalIndex(depth: Int, x: Int, y: Int, targetX: Int, targetY: Int, cache: Cache): Int = when {
    x == 0 && y == 0 || x == targetX && y == targetY -> 0
    x == 0 -> y * 48271
    y == 0 -> x * 16807
    else -> erosionLevel(depth, x - 1, y, targetX, targetY, cache) * erosionLevel(
        depth,
        x,
        y - 1,
        targetX,
        targetY,
        cache
    )
}

internal fun erosionLevel(depth: Int, x: Int, y: Int, targetX: Int, targetY: Int, cache: Cache) =
    cache[x to y] ?: ((geologicalIndex(depth, x, y, targetX, targetY, cache) + depth) % 20183).also {
        cache[x to y] = it
    }