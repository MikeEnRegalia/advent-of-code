package aoc2018.day17

import kotlin.math.max
import kotlin.math.min

fun day17ReservoirResearch(input: String): Pair<Int, Int> {
    val clay = input.loadMap()
    val flowingWater = mutableSetOf<Pos>()
    val stableWater = mutableSetOf<Pos>()

    val maxY = clay.maxOf { it.y }

    fun Pos.supportsWater() = this in clay || this in stableWater

    fun Pos.findBoundary(right: Boolean): Pair<Pos, Boolean> {
        var p = this
        while (true) {
            flowingWater.add(p)
            val next = if (right) p.right() else p.left()
            when {
                !p.below().supportsWater() -> return p to true
                next in clay -> return p to false
                else -> p = next
            }
        }
    }

    fun Pos.bounds(): Bounds {
        val (left, leftIsCliff) = findBoundary(right = false)
        val (right, rightIsCliff) = findBoundary(right = true)
        return Bounds(left, leftIsCliff, right, rightIsCliff)
    }

    fun fromSpring(spring: Pos): List<Pos> {
        if (spring.below() in flowingWater) return listOf()
        var p = spring

        while (true) {
            flowingWater.add(p)
            val below = p.below()
            when {
                below.y > maxY -> return listOf()
                below.supportsWater() -> break
                else -> p = below
            }
        }

        while (true) {
            with(p.bounds()) {
                when {
                    leftIsCliff || rightIsCliff -> return listOf(left, right)
                    else -> for (x in left.x..right.x) stableWater.add(Pos(x, p.y))
                }
            }
            p = p.above()
        }
    }

    val springs = mutableListOf(Pos(500, 0))
    while (springs.isNotEmpty()) {
        springs.addAll(fromSpring(springs.removeFirst()))
    }

    flowingWater.removeIf { w -> w.y !in clay.minOf { it.y }..clay.maxOf { it.y } }

    return flowingWater.union(stableWater).size to stableWater.size
}

internal data class Pos(val x: Int, val y: Int) {
    fun left() = copy(x = x - 1)
    fun right() = copy(x = x + 1)
    fun below() = copy(y = y + 1)
    fun above() = copy(y = y - 1)
}

internal data class Bounds(val left: Pos, val leftIsCliff: Boolean, val right: Pos, val rightIsCliff: Boolean)

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
        return (from..to).map { if (axis == "x") Pos(n.toInt(), it) else Pos(it, n.toInt()) }
    }

internal fun String.loadMap() = split("\n").flatMap { it.toClay() }.toSet()