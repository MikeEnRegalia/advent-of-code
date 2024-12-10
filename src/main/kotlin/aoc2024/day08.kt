package aoc2024

fun main() {
    val grid = generateSequence(::readLine).toList()

    data class Loc(val x: Int, val y: Int, val antenna: Char) {
        fun antinodesFor(other: Loc, f: Int = 1): Set<Loc> {
            val (dx, dy) = f * (x - other.x) to f * (y - other.y)
            val (a1, a2) = Loc(x + dx, y + dy, '#') to Loc(other.x - dx, other.y - dy, '#')
            return sequenceOf(a1, a2).filter { grid.getOrNull(it.y)?.getOrNull(it.x) != null }.toSet()
        }
    }

    val allAntennas = buildMap<Char, Set<Loc>> {
        for (y in grid.indices) for (x in grid[y].indices) grid[y][x].takeIf { it !in ".#" }
            ?.let { compute(it) { _, v -> (v ?: setOf()) + Loc(x, y, it) } }
    }

    val antennaCombinations = allAntennas.values.flatMap { antennas ->
        antennas.flatMap { a -> antennas.filter { it != a }.map { b -> a to b } }
    }

    fun antinodes(simple: Boolean) = antennaCombinations.fold(mutableSetOf<Loc>()) { acc, (a, b) ->
        when {
            simple -> acc.addAll(a.antinodesFor(b))
            else -> {
                var f = 0
                while (true) acc.addAll(a.antinodesFor(b, f++).takeIf { it.isNotEmpty() } ?: break)
            }
        }
        acc
    }

    listOf(true, false).forEach { println(antinodes(simple = it).size) }
}
