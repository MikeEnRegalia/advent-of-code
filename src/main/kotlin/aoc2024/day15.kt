package aoc2024

fun main() {
    val data = generateSequence(::readLine).toList()

    data class Point(val x: Int, val y: Int)

    val originalGrid = data.filter { '#' in it }.flatMapIndexed { y, l ->
        l.mapIndexedNotNull { x, c ->
            if (c != '.') Point(x, y) to c else null
        }
    }.toMap()

    val movements = data.filter { '#' !in it && it.isNotBlank() }.joinToString("")

    val grid = originalGrid.toMutableMap()
    var robot = grid.filterValues { it == '@' }.keys.single()

    fun push(next: (Point) -> Point) {
        val moving = mutableListOf<Point>()
        var point = robot
        while (true) {
            point = next(point)
            when (grid[point]) {
                'O' -> moving += point
                '#' -> return
                else -> {
                    if (moving.isEmpty()) {
                        robot = point
                        return
                    }
                    grid[point] = 'O'
                    grid -= moving.first()
                    robot = moving.first()
                    return
                }
            }
        }
    }

    for (movement in movements) {
        when (movement) {
            '<' -> push { it.copy(x = it.x - 1) }
            '>' -> push { it.copy(x = it.x + 1) }
            '^' -> push { it.copy(y = it.y - 1) }
            'v' -> push { it.copy(y = it.y + 1) }
        }
    }

    println(grid.filterValues { it == 'O' }.keys.sumOf { it.y * 100 + it.x })
}
