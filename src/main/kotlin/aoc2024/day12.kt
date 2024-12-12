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

    data class Fence(val facing: Char, val plot: Plot)

    fun Plot.facing(o: Plot) = when {
        o.x == x - 1 -> 'W'
        o.x == x + 1 -> 'E'
        o.y == y - 1 -> 'N'
        o.y == y + 1 -> 'S'
        else -> throw IllegalStateException()
    }

    fun Plot.fences() =
        adj().filter { grid.getOrNull(it.y)?.getOrNull(it.x) != grid[y][x] }.map { Fence(facing(it), this) }

    fun Area.fenceSides(): Set<Set<Fence>> = buildSet {
        plots.flatMap(Plot::fences).let { areaFences ->
            for (fence in areaFences) {
                if (any { fence in it }) continue

                var toVisit = listOf(fence)
                val side = mutableSetOf<Fence>()
                while (true) {
                    side += toVisit
                    val next = toVisit.flatMap { f ->
                        val neighbors = f.plot.neighbors().toSet()
                        val neighboringFences = neighbors.map { Fence(f.facing, it) }
                            .filter { it in areaFences && it.facing == f.facing && it !in side }
                        neighboringFences
                    }.takeIf { it.isNotEmpty() } ?: break
                    toVisit = next
                }
                add(side)
            }
        }
    }
    println(areas.sumOf { it.plots.size * it.fenceSides().size })
}
