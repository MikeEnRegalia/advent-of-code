package aoc2018.day22

import aoc2018.day22.Tool.*
import java.lang.Integer.MAX_VALUE
import kotlin.math.min

fun day22(depth: Int, targetX: Int, targetY: Int): Int = with(Cave(depth, targetX, targetY)) {
    matrix().sumOf { (x, y) -> type(x, y) }
}

internal fun Cave.type(x: Int, y: Int) = erosionLevel(x, y) % 3

internal data class Cave(val depth: Int, val targetX: Int, val targetY: Int) {
    val erosionCache: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
}

internal fun Cave.calculateErosionLevel(x: Int, y: Int) = (geologicalIndex(x, y) + depth) % 20183

internal fun Cave.erosionLevel(x: Int, y: Int) = erosionCache.computeIfAbsent(x to y) { calculateErosionLevel(x, y) }

internal fun Cave.geologicalIndex(x: Int, y: Int): Int = when {
    x == 0 && y == 0 || x == targetX && y == targetY -> 0
    x == 0 -> y * 48271
    y == 0 -> x * 16807
    else -> erosionLevel(x - 1, y) * erosionLevel(x, y - 1)
}

internal fun Cave.matrix() = (0..targetX).flatMap { x -> (0..targetY).map { y -> x to y } }

fun day22Part2(depth: Int, targetX: Int, targetY: Int): Int? {
    val tools = with(Cave(depth, targetX + 25, targetY + 25)) {
        matrix().map { (x, y) ->
            (x to y) to when (type(x, y)) {
                0 -> listOf(CLIMBING_GEAR, TORCH)
                1 -> listOf(CLIMBING_GEAR, NEITHER)
                else -> listOf(TORCH, NEITHER)
            }
        }
    }.toMap()

    data class Node(val x: Int, val y: Int, val tool: Tool)

    val target = Node(targetX, targetY, TORCH)
    var curr = Node(0, 0, TORCH)
    val distances = mutableMapOf(curr to 0)
    val visited = mutableSetOf<Node>()

    fun tools(x: Int, y: Int) = if (x == target.x && y == target.y) listOf(CLIMBING_GEAR, TORCH) else tools[x to y]!!

    fun Node.adj() = tools(x, y).filter { it != tool }.map { Node(x, y, it) }
        .plus(sequenceOf(x to y + 1, x to y - 1, x + 1 to y, x - 1 to y)
            .filter { (x, y) -> x in 0..targetX + 25 && y in 0..targetY + 25 }
            .flatMap { (x, y) -> tools(x, y).filter { it == tool }.map { Node(x, y, it) } })
        .filter { it !in visited && it != this }

    fun Node.distanceBetween(n: Node): Int {
        val differentPlace = n.x != x || n.y != y
        val differentTool = n.tool != tool
        if (differentPlace && differentTool) throw IllegalStateException()
        return if (differentPlace) 1 else 7
    }

    val currAdj = mutableSetOf<Node>()
    while (true) {
        visited.add(curr)
        currAdj -= curr
        if (curr == target) break

        curr.adj().forEach { node ->
            currAdj += node
            val distance = distances[curr]!! + curr.distanceBetween(node)
            distances[node] = min(distance, distances.getOrDefault(node, MAX_VALUE))
        }
        curr = currAdj.minByOrNull { distances[it]!! } ?: break
    }

    return distances[target]
}

internal enum class Tool {
    TORCH,
    CLIMBING_GEAR,
    NEITHER
}