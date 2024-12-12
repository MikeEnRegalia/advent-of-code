package aoc2024

typealias Plot = Pair<Int, Int>
typealias Area = Pair<Char, Set<Plot>>

fun main() {
    val grid = generateSequence(::readLine).toList()

    fun Plot.neighbors() = sequenceOf(
        copy(first = first - 1),
        copy(first = first + 1),
        copy(second = second - 1),
        copy(second = second + 1)
    ).filter { grid.getOrNull(it.second)?.getOrNull(it.first) == grid[second][first] }

    val allPlots = grid.flatMapIndexed { i, l -> l.indices.map { Pair(it, i) } }

    val areas = mutableSetOf<Area>()
    for (plot in allPlots) {
        if (areas.any { plot in it.second }) continue
        areas += Area(grid[plot.second][plot.first], buildSet {
            add(plot)
            while (true)
                flatMap(Plot::neighbors).filterNot(this::contains).takeIf { it.isNotEmpty() }?.also(::addAll) ?: break
        })
    }

    println(areas.sumOf { it.second.size * it.second.sumOf { 4 - it.neighbors().count() } })
}
