package aoc2024

fun main() {
    val grid = generateSequence(::readLine).toList()
    fun gridAt(x: Int, y: Int) = grid.getOrNull(y)?.getOrNull(x)

    data class Point(val x: Int, val y: Int, val antenna: Char) {
        fun antinodesFor(other: Point, f: Int): Set<Point> {
            val (dx, dy) = f * (x - other.x) to f * (y - other.y)
            val (a1, a2) = Point(x + dx, y + dy, '#') to Point(other.x - dx, other.y - dy, '#')
            return sequenceOf(a1, a2).filter { gridAt(it.x, it.y) != null }.toSet()
        }
    }

    val allAntennas = sequence {
        for (y in grid.indices) for (x in grid[y].indices) {
            grid[y][x].takeIf { it !in ".#" }?.let { yield(Point(x, y, it)) }
        }
    }.toList()

    fun antinodes(simple: Boolean) = buildSet {
        for (antenna in allAntennas.map { it.antenna }.toSet()) {
            val antennas = allAntennas.filter { it.antenna == antenna }.takeIf { it.size > 1 } ?: continue
            for (a in antennas) for (b in antennas.filter { a != it }) when {
                simple -> addAll(a.antinodesFor(b, 1))
                else -> {
                    var f = 0
                    while (true) {
                        val antinodes = a.antinodesFor(b, f).takeIf { it.isNotEmpty() } ?: break
                        addAll(antinodes)
                        f++
                    }
                }
            }
        }
    }.size

    println(antinodes(simple = true))
    println(antinodes(simple = false))
}
