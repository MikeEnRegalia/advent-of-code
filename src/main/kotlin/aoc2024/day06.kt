package aoc2024

fun main() {
    val grid = generateSequence(::readLine).toList()

    val directions = listOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)

    data class Point(val x: Int, val y: Int) {
        fun next(dir: Int) = directions[dir].let { (dx, dy) -> copy(x = x + dx, y = y + dy) }
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

    val visited = buildSet {
        var curr = Pos(start, 0)
        while (true) {
            add(curr.p)
            val next = curr.next()
            when (next.p.content()) {
                null -> break
                '#' -> curr = curr.turn()
                else -> curr = next
            }
        }
    }
    println(visited.size)

    val loopingObstacles = (visited - start).filter { point ->
        val walked = mutableSetOf<Pos>()
        var curr = Pos(start, 0)
        while (walked.add(curr)) {
            val next = curr.next()
            curr = when (if (next.p == point) '#' else next.p.content()) {
                null -> return@filter false
                '#' -> curr.turn()
                else -> next
            }
        }
        true
    }
    println(loopingObstacles.size)
}
