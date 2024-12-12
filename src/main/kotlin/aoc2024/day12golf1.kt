package aoc2024

fun main() {
    val grid = generateSequence(::readLine).toList()

    data class Plot(val x: Int, val y: Int) {
        fun adj() = sequenceOf(copy(x = x - 1), copy(x = x + 1), copy(y = y - 1), copy(y = y + 1))
        fun neighbors() = adj().filter { grid.getOrNull(it.y)?.getOrNull(it.x) == grid[y][x] }
    }

    data class Area(val id: Char, val plots: Set<Plot>)

    val areas = mutableSetOf<Area>()
    for (y in grid.indices) for (x in grid[0].indices) {
        val plot = Plot(x, y).takeIf { areas.none { a -> it in a.plots } } ?: continue
        val areaPlots = mutableSetOf(plot)

        var newPlots: List<Plot>
        do {
            newPlots = areaPlots.flatMap(Plot::neighbors).filter { it !in areaPlots }
            areaPlots += newPlots
        } while (newPlots.isNotEmpty())

        areas += Area(grid[y][x], areaPlots.toSet())
    }
    println(areas.sumOf { it.plots.size * it.plots.sumOf { 4 - it.neighbors().count() } })
}
