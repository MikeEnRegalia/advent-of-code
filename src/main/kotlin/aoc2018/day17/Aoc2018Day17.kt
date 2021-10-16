package aoc2018.day17

fun day17ReservoirResearch(input: String): Int {
    val clay = input.loadMap()
    val flowingWater = mutableSetOf<Pos>()
    val stableWater = mutableSetOf<Pos>()

    val maxY = clay.maxOf { it.y }
    val minX = clay.minOf { it.x }
    val maxX = clay.maxOf { it.x }

    fun Pos.isClay() = this in clay
    fun Pos.isStableWater() = this in stableWater
    fun Pos.supportsWater() = isClay() || isStableWater()

    fun Pos.boundsIfBasin(): Pair<Int, Int>? {
        var right = x
        while (true) {
            if (right > maxX || !Pos(right, y + 1).supportsWater()) return null
            if (Pos(right + 1, y).isClay()) break
            right++
        }

        var left = x
        while (true) {
            if (left < minX || !Pos(left, y + 1).supportsWater()) return null
            if (Pos(left - 1, y).isClay()) break
            left--
        }

        return (left to right).also { println(it) }
    }

    fun fromSpring(spring: Pos): List<Pos> {
        var p = spring
        while (true) {
            p = p.below()
            if (p.y >= maxY) return listOf()
            if (p.supportsWater()) {
                p = p.above()
                break
            }
            flowingWater.add(p)
        }

        val (left, right) = p.boundsIfBasin() ?: return listOf()

        for (x in left..right) stableWater.add(Pos(x, p.y))

        return listOf()
    }

    var springs = listOf(Pos(500, 0))
    while (springs.isNotEmpty()) {
        springs = springs.flatMap { fromSpring(it) }
    }

    val water = flowingWater.union(stableWater).toMutableSet()

    println("----------------------")
    println(clay.render(flowingWater, stableWater))

    water.removeIf { w -> w.y !in clay.minOf { it.y }..clay.maxOf { it.y } }

    return water.size
}

internal data class Pos(val x: Int, val y: Int) {
    fun below() = copy(y = y + 1)
    fun above() = copy(y = y - 1)
}

internal fun Set<Pos>.render(flowingWater: Set<Pos>, stableWater: Set<Pos>, spring: Pos = Pos(500, 0)): String {
    val from = Pos(minOf { it.x }, 0)
    val to = Pos(maxOf { it.x }, maxOf { it.y })

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