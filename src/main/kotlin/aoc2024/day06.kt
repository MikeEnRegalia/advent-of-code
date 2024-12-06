package aoc2024

fun main() {
    val grid = generateSequence(::readLine).toList()

    data class Point(val x: Int, val y: Int) {
        fun next(dir: Int) = when (dir) {
            0 -> copy(y = y - 1)
            1 -> copy(x = x + 1)
            2 -> copy(y = y + 1)
            3 -> copy(x = x - 1)
            else -> throw IllegalArgumentException("Unknown direction")
        }
    }

    fun points() = sequence {
        for (y in grid.indices) for (x in grid[y].indices) yield(Point(x, y))
    }

    fun gridAt(point: Point) = grid.getOrNull(point.y)?.getOrNull(point.x)

    val start = points().single { gridAt(it) == '^' }

    fun part1(): Int {
        var curr = start
        var dir = 0
        val visited = mutableSetOf(curr)
        while (true) {
            val next = curr.next(dir)
            when (gridAt(next)) {
                null -> return visited.size
                '#' -> dir = (dir + 1) % 4
                else -> {
                    curr = next
                    visited += curr
                }
            }
        }
    }
    println(part1())

    val part2 = points().filter { point ->
        var curr = start
        var dir = 0
        val walked = mutableSetOf(curr to dir)
        while (true) {
            val next = curr.next(dir)
            when (if (next == point) '#' else gridAt(next)) {
                null -> return@filter false
                '#' -> dir = (dir + 1) % 4
                else -> {
                    curr = next
                    if (!walked.add(curr to dir)) break
                }
            }
        }
        true
    }.count()
    println(part2)
}
