package aoc2018.day17

import kotlin.math.max
import kotlin.math.min

fun day17ReservoirResearch(input: String): Pair<Int, Int> {
    val clay = input.loadMap()
    val flowingWater = mutableSetOf<Pos>()
    val stableWater = mutableSetOf<Pos>()

    val maxY = clay.maxOf { it.y }

    fun Pos.isClay() = this in clay
    fun Pos.isStableWater() = this in stableWater
    fun Pos.isFlowingWater() = this in flowingWater
    fun Pos.supportsWater() = isClay() || isStableWater()

    fun Pos.findBoundary(right: Boolean): Pair<Pos, Boolean> {
        fun Pos.next(): Pos = if (right) right() else left()
        var p = this
        while (true) {
            flowingWater.add(p)
            val next = p.next()
            if (!p.below().supportsWater()) return p to true
            if (next.isClay()) return p to false
            p = next
        }
    }

    fun Pos.bounds(): Bounds {
        val (left, leftIsCliff) = findBoundary(right = false)
        val (right, rightIsCliff) = findBoundary(right = true)
        return Bounds(left, leftIsCliff, right, rightIsCliff)
    }

    fun fromSpring(spring: Pos): List<Pos> {
        if (spring.below().isFlowingWater()) return listOf()
        var p = spring
        while (true) {
            p = p.below()
            if (p.y > maxY) return listOf()
            if (p.supportsWater()) {
                p = p.above()
                break
            }
            flowingWater.add(p)
        }

        while (true) {
            val bounds = p.bounds()
            if (!bounds.isBasin()) {
                return listOf(bounds.left, bounds.right)
            }
            val (left, _, right, _) = bounds

            for (x in left.x..right.x) {
                stableWater.add(Pos(x, p.y))
            }
            p = p.above()
        }
    }

    var springs = listOf(Pos(500, 0))
    while (springs.isNotEmpty()) {
        springs = springs.flatMap { fromSpring(it) }
    }

    flowingWater.removeIf { w -> w.y !in clay.minOf { it.y }..clay.maxOf { it.y } }
    stableWater.removeIf { w -> w.y !in clay.minOf { it.y }..clay.maxOf { it.y } }

    return flowingWater.union(stableWater).size to stableWater.size
}

internal data class Pos(val x: Int, val y: Int) {
    fun left() = copy(x = x - 1)
    fun right() = copy(x = x + 1)
    fun below() = copy(y = y + 1)
    fun above() = copy(y = y - 1)
}

internal data class Bounds(val left: Pos, val leftIsCliff: Boolean, val right: Pos, val rightIsCliff: Boolean) {
    fun isBasin() = !leftIsCliff && !rightIsCliff
}

@Suppress("unused")
internal fun Set<Pos>.render(flowingWater: Set<Pos>, stableWater: Set<Pos>, spring: Pos = Pos(500, 0)): String {
    val from = Pos(min(minOf { it.x }, flowingWater.minOf { it.x }), 0)
    val to = Pos(max(maxOf { it.x }, flowingWater.maxOf { it.x }), maxOf { it.y })

    return (from.y..to.y).joinToString("\n") { y ->
        (from.x..to.x).joinToString("") { x ->
            when {
                stableWater.contains(Pos(x, y)) -> "~"
                flowingWater.contains(Pos(x, y)) -> "|"
                Pos(x, y) == spring -> "+"
                contains(Pos(x, y)) -> "#"
                else -> "."
            }
        }
    }
}

internal fun String.toClay(): List<Pos> = split(Regex(", "))
    .map { it.split("=") }
    .let { tokens ->
        val (axis, n) = tokens[0]
        val (_, rangeToken) = tokens[1]
        val (from, to) = rangeToken.split(Regex("""\.\.""")).map { it.toInt() }
        val range = from..to
        return if (axis == "x") range.map { Pos(n.toInt(), it) }
        else range.map { Pos(it, n.toInt()) }
    }

internal fun String.loadMap() = split("\n").flatMap { it.toClay() }.toSet()