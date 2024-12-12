package aoc2024

fun main() {
    val grid = generateSequence(::readLine).toList()

    data class Plot(val x: Int, val y: Int)

    fun Plot.adj() = sequenceOf(copy(x = x - 1), copy(x = x + 1), copy(y = y - 1), copy(y = y + 1))
    fun Plot.neighbors() = adj().filter { grid.getOrNull(it.y)?.getOrNull(it.x) == grid[y][x] }

    data class Area(val id: Char, val plots: Set<Plot>)

    val areas = mutableSetOf<Area>()

    for (y in grid.indices) for (x in grid[0].indices) {
        val plot = Plot(x, y).takeIf { areas.none { a -> it in a.plots } } ?: continue
        val areaPlots = mutableSetOf(plot)

        var newPlots: List<Plot>
        do {
            newPlots = areaPlots.flatMap { it.neighbors() }.filter { it !in areaPlots }
            areaPlots += newPlots
        } while (newPlots.isNotEmpty())

        areas += Area(grid[y][x], areaPlots.toSet())
    }
    println(areas.sumOf { it.plots.size * it.plots.sumOf { 4 - it.neighbors().count() } })

    data class Fence(val facing: Char, val plot: Plot)

    fun Area.fenceSides(): Set<Set<Fence>> = plots.flatMap { p ->
        p.adj()
            .filter { grid.getOrNull(it.y)?.getOrNull(it.x) != grid[p.y][p.x] }
            .map { o ->
                val facing = when {
                    o.x == p.x - 1 -> 'W'
                    o.x == p.x + 1 -> 'E'
                    o.y == p.y - 1 -> 'N'
                    o.y == p.y + 1 -> 'S'
                    else -> throw IllegalStateException()
                }
                Fence(facing, p)
            }
    }.toSet().let { areaFences ->
        val sides = mutableSetOf<Set<Fence>>()
        val visited = mutableSetOf<Fence>()
        for (fence in areaFences) {
            if (fence in visited) continue

            val toVisit = mutableSetOf(fence)
            val side = mutableSetOf<Fence>()
            while (true) {
                side += toVisit
                visited += toVisit
                val next = toVisit.flatMap { f ->
                    val neighbors = f.plot.neighbors().toSet()
                    val neighboringFences = neighbors.map { Fence(f.facing, it) }
                        .filter { it in areaFences && it.facing == f.facing && it !in visited }
                    neighboringFences
                }.takeIf { it.isNotEmpty() } ?: break
                toVisit.clear()
                toVisit += next
            }
            sides += side
        }
        sides
    }
    println(areas.sumOf { it.plots.size * it.fenceSides().size })
}
