package aoc2018.day17

fun day17ReservoirResearch(input: String) {
    val map = input.loadMap()
    println(map.render())
}

internal fun Set<Pos>.render(spring: Pos = Pos(500, 0)) =
    (0..maxOf { it.y }).joinToString("\n") { y ->
        (minOf { it.x }..maxOf { it.x }).joinToString("") { x ->
            when {
                Pos(x, y) == spring -> "+"
                contains(Pos(x, y)) -> "#"
                else -> "."
            }
        }
    }

internal data class Pos(val x: Int, val y: Int)

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

internal fun String.loadMap() = split("\n")
    .flatMap { it.toClay() }.toSet()
