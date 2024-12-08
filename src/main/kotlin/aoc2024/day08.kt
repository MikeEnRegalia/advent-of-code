package aoc2024

fun main() {
    val grid = generateSequence(::readLine).toList()
    fun gridAt(x: Int, y: Int) = grid.getOrNull(y)?.getOrNull(x)

    data class Point(val x: Int, val y: Int, val antenna: Char) {
        fun antinodesFor(other: Point, f: Int): Set<Point> {
            val dx = f * (x - other.x)
            val dy = f * (y - other.y)
            val a1 = Point(x + dx, y + dy, '#')
            val a2 = Point(other.x - dx, other.y - dy, '#')
            return buildSet {
                if (gridAt(a1.x, a1.y) != null) add(a1)
                if (gridAt(a2.x, a2.y) != null) add(a2)
            }
        }
    }

    val antennas = sequence {
        for (y in grid.indices) for (x in grid[y].indices) {
            val c = grid[y][x]
            if (c !in ".#") yield(Point(x, y, c))
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
