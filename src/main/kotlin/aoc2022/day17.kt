package aoc2022

import java.util.*

fun main() {
    val jets = generateSequence(::readlnOrNull).first().split("").filter { it.isNotBlank() }

    data class Pos(val x: Int, val y: Int) {
        operator fun plus(p: Pos) = Pos(x + p.x, y + p.y)
        operator fun minus(p: Pos) = Pos(x - p.x, y - p.y)
    }

    data class Shape(val points: Set<Pos>, val pos: Pos = Pos(0, 0)) {
        fun minX() = points.minOf { it.x }
        fun maxX() = points.maxOf { it.x }
        fun minY() = points.minOf { it.y }
        fun maxY() = points.maxOf { it.y }
        fun at(p: Pos) = copy(pos = p, points = points.map { it - pos + p }.toSet())
        fun left() = at(pos + Pos(-1, 0))
        fun right() = at(pos + Pos(1, 0))
        fun down() = at(pos + Pos(0, -1))
        fun intersects(shape: Shape) = points.intersect(shape.points).isNotEmpty()
        fun similar(shape: Shape) = (points.first().y - shape.points.first().y).let { dy ->
            points.zip(shape.points).all { (a, b) -> b + Pos(0, dy) == a }
        }
    }

    fun Iterable<Shape>.merge() = flatMap { it.points }.toSet().let { points ->
        Shape(points, Pos(points.minOf { it.x }, points.minOf { it.y }))
    }

    val shapes = listOf(
        Shape(setOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(3, 0))),
        Shape(setOf(Pos(0, 1), Pos(1, 1), Pos(2, 1), Pos(1, 0), Pos(1, 2))),
        Shape(setOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(2, 1), Pos(2, 2))),
        Shape(setOf(Pos(0, 0), Pos(0, 1), Pos(0, 2), Pos(0, 3))),
        Shape(setOf(Pos(0, 0), Pos(1, 0), Pos(0, 1), Pos(1, 1)))
    )

    var shapesTick = 0
    fun newShape() = shapes[shapesTick].also { shapesTick = (shapesTick + 1) % shapes.size }

    var jetsTick = 0
    fun newJet() = jets[jetsTick].also { jetsTick = (jetsTick + 1) % jets.size }

    val stuckShapes = ArrayDeque<Shape>()

    var shape = newShape().at(Pos(2, 4))
    val xRange = 0..6
    var nStuckShapes = 0

    data class State(val jetsTick: Int, val shapesTick: Int, val stuckShapes: Shape)

    val states = mutableMapOf<Int, State>()
    val history = mutableMapOf<Pair<Int, Int>, Pair<Int, Iterable<Shape>>>()
    val heights = mutableMapOf<Int, Int>()

    var cycleStart: Int? = null

    var part1: Int? = null
    var part2: Long? = null
    while (part1 == null || part2 == null) {
        with(shape) {
            shape = when (newJet()) {
                "<" -> if (minX() > xRange.first && stuckShapes.none { it.intersects(left()) }) left() else this

                else -> if (maxX() < xRange.last && stuckShapes.none { it.intersects(right()) }) right() else this
            }
        }

        if (shape.down().minY() != 0 && !stuckShapes.any { it.intersects(shape.down()) }) {
            shape = shape.down()
            continue
        }

        stuckShapes += shape
        nStuckShapes++
        states[nStuckShapes] = State(jetsTick, shapesTick, stuckShapes.merge())

        heights[nStuckShapes] = stuckShapes.maxOf { it.maxY() }
        if (stuckShapes.size > 40) {
            stuckShapes.removeFirst()

            val k = Pair(jetsTick, shapesTick)
            val prev = history[k]
            if (prev != null) {
                val prevShape = prev.second.merge()
                val currShape = stuckShapes.merge()
                if (prevShape.similar(currShape) && cycleStart == null) {
                    val y1 = prevShape.maxY()
                    val y2 = currShape.maxY()
                    val cycleHeight = y2 - y1
                    val cycleCount = nStuckShapes - prev.first
                    cycleStart = prev.first

                    val fullCycles = (1_000_000_000_000 - cycleStart) / cycleCount
                    val partialCycle = ((1_000_000_000_000 - cycleStart) % cycleCount).toInt()
                    val remIndex = states[cycleStart + partialCycle]!!.stuckShapes.maxY() - cycleStart
                    part2 = prev.first + (fullCycles * cycleHeight) + remIndex
                }
            }
            history[k] = nStuckShapes to stuckShapes.toList()
        }

        if (nStuckShapes == 2022) {
            part1 = stuckShapes.maxOf { it.maxY() }
        }

        shape = newShape().at(Pos(2, stuckShapes.maxOf { it.maxY() } + 4))
    }

    println(part1)
    println(part2)
}

