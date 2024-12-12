package aoc2024

typealias Plot = Pair<Int, Int>
typealias Area = Pair<Char, Set<Plot>>

fun main() {
    val grid = generateSequence(::readLine).toList()

    fun Plot.adj() = sequenceOf(
        copy(first = first - 1),
        copy(first = first + 1),
        copy(second = second - 1),
        copy(second = second + 1)
    )

    fun Plot.neighbors() = adj().filter { grid.getOrNull(it.second)?.getOrNull(it.first) == grid[second][first] }

    val allPlots = grid.flatMapIndexed { i, l -> l.indices.map { Pair(it, i) } }

    val areas = mutableSetOf<Area>()
    for (plot in allPlots) {
        if (areas.any { plot in it.second }) continue
        areas += Area(grid[plot.second][plot.first], buildSet {
            add(plot)
            var newPlots: List<Plot>
            do {
                newPlots = flatMap(Plot::neighbors).filter { it !in this }
                addAll(newPlots)
            } while (newPlots.isNotEmpty())
        })
    }
    println(areas.sumOf { it.second.size * it.second.sumOf { 4 - it.neighbors().count() } })
}
