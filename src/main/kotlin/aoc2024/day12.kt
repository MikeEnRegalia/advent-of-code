package aoc2024

fun main() {
    val grid = generateSequence(::readLine).toList()

    data class Plot(val x: Int, val y: Int)

    fun Plot.neighbors() = sequenceOf(copy(x = x - 1), copy(x = x + 1), copy(y = y - 1), copy(y = y + 1))
        .filter { grid.getOrNull(it.y)?.getOrNull(it.x) == grid[y][x] }

    fun Plot.fences() = 4 - neighbors().count()

    data class Area(val id: Char, val plots: Set<Plot>)

    val areas = mutableSetOf<Area>()
    val visited = mutableSetOf<Plot>()

    for (y in grid.indices) {
        for (x in grid[0].indices) {
            val plot = Plot(x, y).takeIf { it !in visited } ?: continue
            val areaPlots = mutableSetOf(plot)
            while (true) {
                val newPlots = areaPlots.flatMap { it.neighbors() }.toSet()
                    .filter { it !in visited }.takeIf { it.isNotEmpty() } ?: break
                areaPlots += newPlots
                visited += newPlots
            }
            areas += Area(grid[y][x], areaPlots.toSet())
        }
    }
    println(areas.map { it.id }.distinct().count())
    println(areas.sumOf { it.plots.size * it.plots.sumOf(Plot::fences) })
}
