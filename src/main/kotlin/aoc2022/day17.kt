package aoc2022

fun main() = day00(String(System.`in`.readAllBytes())).forEach(::println)

private fun day00(input: String): List<Any?> {
    val jets = input.split("").filter { it.isNotBlank() }.also(::println)

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
    }

    val shapes = listOf(
        Shape(setOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(3, 0))),
        Shape(setOf(Pos(0, 1), Pos(1, 1), Pos(2, 1), Pos(1, 0), Pos(1, 2))),
        Shape(setOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(2, 1), Pos(2, 2))),
        Shape(setOf(Pos(0, 0), Pos(0, 1), Pos(0, 2), Pos(0, 3))),
        Shape(setOf(Pos(0, 0), Pos(1, 0), Pos(0, 1), Pos(1, 1)))
    )

    var shapesTick = 0
    fun newShape() = shapes[shapesTick % shapes.size].also { shapesTick++ }

    var jetsTick = 0
    fun newJet() = jets[jetsTick % jets.size].also { jetsTick++ }

    val stuckShapes = mutableListOf<Shape>()
    var shape = newShape().at(Pos(2, 4))
    val xRange = 0..6

    fun print() {
        for (y in (stuckShapes.maxOfOrNull { it.maxY() } ?: shape.maxY()) downTo 1) {
            println((0..6).map { x -> when (Pos(x, y)) {
                in stuckShapes.flatMap { it.points } -> "#"
                in shape.points -> "@"
                else -> "."
            } }.joinToString(""))
        }
    }

    while (true) {
        if (stuckShapes.size <= 2) print()
        when (val newJet = newJet()) {
            "<" -> if (shape.minX() > xRange.first && stuckShapes.none { it.intersects(shape.left()) }) shape =
                shape.left()

            ">" -> if (shape.maxX() < xRange.last && stuckShapes.none { it.intersects(shape.right()) }) shape =
                shape.right()
            else -> throw IllegalArgumentException(newJet)
        }
        if (stuckShapes.size <= 2) print()

        if (shape.down().minY() == 0 || stuckShapes.any { it.intersects(shape.down()) }) {
            stuckShapes += shape
            if (stuckShapes.size <= 2) print()
            shape = newShape().at(Pos(2, stuckShapes.maxOf { it.maxY() } + 4))
            if (stuckShapes.size == 2022) {
                println(stuckShapes.maxOf { it.maxY() })
                break
            }
        } else shape = shape.down()
    }

    return listOf(null, null)
}

