package aoc2018.day17

import kotlin.math.max
import kotlin.math.min

fun day17ReservoirResearch(input: String): Int {
    val clay = input.loadMap()
    val flowingWater = mutableSetOf<Pos>()
    val stableWater = mutableSetOf<Pos>()

    val maxY = clay.maxOf { it.y }

    fun Pos.isClay() = this in clay
    fun Pos.isStableWater() = this in stableWater
    fun Pos.isFlowingWater() = this in flowingWater
    fun Pos.supportsWater() = isClay() || isStableWater()

    fun Pos.boundsIfBasin(): Pair<Int, Int>? {
        var right = x
        while (true) {
            if (!Pos(right, y + 1).supportsWater()) return null
            if (Pos(right + 1, y).isClay()) break
            right++
        }

        var left = x
        while (true) {
            if (!Pos(left, y + 1).supportsWater()) return null
            if (Pos(left - 1, y).isClay()) break
            left--
        }

        return left to right
    }

    fun Pos.cliffs(): List<Pos> {
        var right = x
        var rightIsCliff = true
        while (true) {
            flowingWater.add(Pos(right, y))
            if (!Pos(right, y + 1).supportsWater()) break
            if (Pos(right + 1, y).isClay()) {
                rightIsCliff = false
                break
            }
            right++
        }

        var left = x
        var leftIsCliff = true
        while (true) {
            flowingWater.add(Pos(left, y))
            if (!Pos(left, y + 1).supportsWater()) break
            if (Pos(left - 1, y).isClay()) {
                leftIsCliff = false
                break
            }
            left--
        }

        return listOfNotNull(
            if (leftIsCliff) Pos(left, y) else null,
            if (rightIsCliff) Pos(right, y) else null
        )
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
            val (left, right) = p.boundsIfBasin() ?: return p.cliffs()

            for (x in left..right) {
                stableWater.add(Pos(x, p.y))
            }
            p = p.above()
        }
    }

    var springs = listOf(Pos(500, 0))
    while (springs.isNotEmpty()) {
        springs = springs.flatMap { fromSpring(it) }
    }

    val water = flowingWater.union(stableWater).toMutableSet()

    water.removeIf { w -> w.y !in clay.minOf { it.y }..clay.maxOf { it.y } }

    return water.size
}

internal data class Pos(val x: Int, val y: Int) {
    fun below() = copy(y = y + 1)
    fun above() = copy(y = y - 1)
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