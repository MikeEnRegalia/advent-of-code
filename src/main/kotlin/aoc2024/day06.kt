package aoc2024

fun main() {
    data class Point(val x: Int, val y: Int)

    val grid = generateSequence(::readLine).toList()
    fun gridAt(point: Point) = grid.getOrNull(point.y)?.getOrNull(point.x)
    fun points() = sequence {
        for (y in grid.indices) for (x in grid[y].indices) yield(Point(x, y))
    }

    val start = points().single { gridAt(it) == '^' }

    var curr = start
    var dir = 0
    val visited = mutableSetOf(curr)
    while (true) {
        println("$curr $dir")
        val next = with(curr) {
            when (dir) {
                0 -> copy(y = y - 1)
                1 -> copy(x = x + 1)
                2 -> copy(y = y + 1)
                3 -> copy(x = x - 1)
                else -> throw IllegalArgumentException("Unknown direction")
            }
        }
        when (gridAt(next)) {
            null -> break
            '#' -> {
                dir = (dir + 1) % 4
                continue
            }

            else -> {
                curr = next
                visited += curr
            }
        }
    }
    println(visited.size)
}
