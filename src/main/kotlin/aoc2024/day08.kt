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

    val antennas = sequence {
        for (y in grid.indices) for (x in grid[y].indices) {
            grid[y][x].takeIf { it !in ".#" }?.let { yield(Point(x, y, it))}
        }
    }.toList()

    fun antinodes(full: Boolean): Set<Point> {
        return buildSet {
            for (antenna in antennas.map { it.antenna }.toSet()) {
                val interferingAntennas = antennas.filter { it.antenna == antenna }
                if (interferingAntennas.size < 2) continue

                for (a in interferingAntennas) {
                    for (b in interferingAntennas.filter { a != it }) {
                        if (full) {
                            var f = 0
                            while (true) {
                                val antinodes = a.antinodesFor(b, f)
                                if (antinodes.isEmpty()) break
                                addAll(antinodes)
                                f++
                            }

                        } else {
                            addAll(a.antinodesFor(b, 1))
                        }
                    }
                }
            }
        }
    }

    println(antinodes(full = false).size)
    println(antinodes(full = true).size)
}
