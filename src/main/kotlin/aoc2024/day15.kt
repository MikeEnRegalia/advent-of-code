package aoc2024

fun main() {
    val originalData = generateSequence(::readLine).toList()

    data class Point(val x: Int, val y: Int)

    val movements = originalData.filter { '#' !in it && it.isNotBlank() }.joinToString("")

    fun part1() {
        val startGrid = originalData.filter { '#' in it }.flatMapIndexed { y, l ->
            l.mapIndexedNotNull { x, c ->
                if (c != '.') Point(x, y) to c else null
            }
        }.toMap()

        val grid = startGrid.toMutableMap()
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
    part1()

    fun part2() {
        val part2Data = originalData.map {
            it.map { c ->
                when (c) {
                    '#' -> "##"
                    'O' -> "[]"
                    '.' -> ".."
                    '@' -> "@."
                    else -> "$c"
                }
            }.joinToString("")
        }

        val startGrid = part2Data.filter { '#' in it }.flatMapIndexed { y, l ->
            l.mapIndexedNotNull { x, c -> Point(x, y) to c }
        }.toMap()

        val grid = startGrid.toMutableMap()
        var robot = grid.filterValues { it == '@' }.keys.single()

        fun push(dx: Int, dy: Int, expand: Boolean) {
            val moving = mutableSetOf(robot)

            while (true) {
                val newPoints = moving.map { it.copy(x = it.x + dx, y = it.y + dy) }.filter { it !in moving }
                if (newPoints.any { grid[it] == '#' }) return
                if (newPoints.all { grid[it] == '.' }) break
                moving.addAll(newPoints.filter { grid[it] != '.' })
                if (expand) {
                    moving.apply {
                        addAll(filter { grid[it] == '[' }.map { it.copy(x = it.x + 1) })
                        addAll(filter { grid[it] == ']' }.map { it.copy(x = it.x - 1) })
                    }
                }
            }
            //println("robot $robot moving: $moving")

            val newPoints = moving.map { it to grid.getValue(it) }
            newPoints.forEach { (p) -> grid[p] = '.' }
            newPoints.forEach { (p, c) -> grid[p.copy(x = p.x + dx, y = p.y + dy)] = c }
            robot = robot.copy(x = robot.x + dx, y = robot.y + dy)
        }

        fun printGrid(before: Map<Point, Char>) {
            if (before == grid) return
            for (y in 0..grid.maxOf { it.key.y }) {
                for (x in 0..grid.maxOf { it.key.x }) Point(x, y).let { print(before[it] ?: ' ') }
                print("    ")
                for (x in 0..grid.maxOf { it.key.x }) Point(x, y).let {
                    print(
                        if (it == robot) 'x' else grid[it] ?: ' '
                    )
                }
                println()
            }
            println()
        }

        val gridBefore = grid.toMap()

        for ((i, movement) in movements.withIndex()) {
            when (movement) {
                '<' -> push(-1, 0, false)
                '>' -> push(1, 0, false)
                '^' -> push(0, -1, true)
                'v' -> push(0, 1, true)
            }
        }
        printGrid(gridBefore)

        println(grid.filterValues { it == '[' }.keys.sumOf { it.y * 100 + it.x })
    }
    part2()
}
