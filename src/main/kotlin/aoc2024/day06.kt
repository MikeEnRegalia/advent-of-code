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
        fun content() = grid.getOrNull(y)?.getOrNull(x)
    }

    data class Pos(val p: Point, val dir: Int) {
        fun next() = copy(p = p.next(dir))
        fun turn() = copy(dir = (dir + 1) % 4)
    }

    fun points() = sequence {
        for (y in grid.indices) for (x in grid[y].indices) yield(Point(x, y))
    }

    val start = points().single { it.content() == '^' }

    fun part1(): Int {
        var curr = Pos(start, 0)
        val visited = mutableSetOf(curr.p)
        while (true) {
            val next = curr.next()
            when (next.p.content()) {
                null -> return visited.size
                '#' -> curr = curr.turn()
                else -> {
                    curr = next
                    visited += curr.p
                }
            }
        }
    }

    fun part2() = points().count { point ->
        var curr = Pos(start, 0)
        val walked = mutableSetOf(curr)
        while (true) {
            val next = curr.next()
            when (if (next.p == point) '#' else next.p.content()) {
                null -> return@count false
                '#' -> curr = curr.turn()
                else -> {
                    curr = next
                    if (!walked.add(curr)) break
                }
            }
        }
        true
    }

    println(part1())
    println(part2())
}
