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

fun day22Part2(depth: Int, targetX: Int, targetY: Int): Int {
    val cave = with(Cave(depth, targetX * 3, targetY * 3)) {
        matrix().map { (x, y) ->
            (x to y) to when (type(x, y)) {
                0 -> listOf(CLIMBING_GEAR, TORCH)
                1 -> listOf(CLIMBING_GEAR, NEITHER)
                else -> listOf(TORCH, NEITHER)
            }
        }
    }.toMap()

    data class Node(val x: Int, val y: Int, val tool: Tool) {
    }

    var curr = Node(0, 0, TORCH)
    val distances = mutableMapOf(curr to 0)
    val visited = mutableSetOf<Node>()

    val target = Node(targetX, targetY, TORCH)

    fun Node.adj() = cave[x to y]!!.filter { it != tool }.map { Node(x, y, it) }
        .plus(with(this) { sequenceOf(x to y + 1, x to y - 1, x + 1 to y, x - 1 to y) }
            .filter { (x, y) -> x in 0..targetX * 3 && y in 0..targetY * 3 }
            .flatMap { (x, y) -> cave[x to y]!!.filter { it == tool }.map { Node(x, y, it) } })
        .filter { it !in visited && it != this }

    fun Node.distanceBetween(n: Node) = (if (n.x == x && n.y == y) 0 else 1) + (if (n.tool != tool) 7 else 0)

    while (true) {
        val adj = curr.adj()

        visited.add(curr)
        adj.forEach { node ->
            val distance = distances[curr]!! + curr.distanceBetween(node)
            distances[node] = min(distance, distances.getOrDefault(node, MAX_VALUE))
        }
        if (curr == target) break
        curr = visited.flatMap(Node::adj).minByOrNull { distances[it]!! }!!
    }

    return distances[target]!!
}

internal enum class Tool {
    TORCH,
    CLIMBING_GEAR,
    NEITHER
}