package aoc2022

fun main() {
    val lines = generateSequence(::readlnOrNull).toList()
    val mazeLines = lines.dropLast(2)
    val maze = mazeLines.map { line ->
        CharArray(mazeLines.maxOf { it.length }) { i -> if (i > line.lastIndex) ' ' else line[i] }
    }.toTypedArray()
    val width = maze[0].size
    val height = maze.size

    val instructions: List<Any> = lines.last().asSequence().fold(mutableListOf<String>()) { acc, c ->
        acc.apply {
            when {
                c.isDigit() -> {
                    if (isEmpty() || last().toIntOrNull() == null) add("")
                    set(lastIndex, last() + c)
                }
                else -> add(c.toString())
            }
        }
    }.map { it.toIntOrNull() ?: it }.toList()

    data class Pos(val x: Int, val y: Int) {
        override fun toString() = "($x,$y)"
        fun neighbors() = listOf(
            rightOrWrap(),
            downOrWrap(),
            leftOrWrap(),
            upOrWrap()
        ).map {
            when (maze[it.y][it.x]) {
                '#' -> this
                '.' -> it
                else -> throw IllegalStateException()
            }
        }

        fun directNeighbor(facing: Int) = when (facing) {
            0 -> if (x + 1 < width && maze[y][x + 1] != ' ') copy(x = x + 1) else null
            1 -> if (y + 1 < height && maze[y + 1][x] != ' ') copy(y = y + 1) else null
            2 -> if (x - 1 >= 0 && maze[y][x - 1] != ' ') copy(x = x - 1) else null
            3 -> if (y - 1 >= 0 && maze[y - 1][x] != ' ') copy(y = y - 1) else null
            else -> throw IllegalArgumentException(facing.toString())
        }

        fun rightOrWrap() = directNeighbor(0) ?: copy(x = maze[y].indexOfFirst { it != ' ' })
        fun downOrWrap() = directNeighbor(1) ?: copy(y = maze.indexOfFirst { it[x] != ' ' })
        fun leftOrWrap() = directNeighbor(2) ?: copy(x = maze[y].indexOfLast { it != ' ' })
        fun upOrWrap() = directNeighbor(3) ?: copy(y = maze.indexOfLast { it[x] != ' ' })
    }

    var pos = Pos(maze[0].indexOf('.'), 0)
    var facing = 0
    for (instruction in instructions) {
        when (instruction) {
            is Int -> repeat(instruction) { pos = pos.neighbors()[facing] }
            "L" -> facing = (4 + facing - 1) % 4
            "R" -> facing = (facing + 1) % 4
        }
    }
    println(1000 * (pos.y + 1) + 4 * (pos.x + 1) + facing)

    pos = Pos(maze[0].indexOf('.'), 0)
    facing = 0

    // NOTE: Obviously this solution is catered specifically towards my puzzle input.
    // It would be enormously difficult to automatically detect the 2D layout of the cube.
    fun Pos.neighborCube(facing: Int): Pair<Pos, Int> {
        val direct = directNeighbor(facing)
        if (direct != null) return direct to facing
        val newPos: Pos
        val newFacing: Int
        when {
            x == 149 && y in 0..49 && facing == 0 -> {
                newPos = Pos(x = 99, y = 149 - y)
                newFacing = 2
                // ok
            }

            x == 99 && y in 100..149 && facing == 0 -> {
                newPos = Pos(x = 149, y = 149 - y)
                newFacing = 2
                // ok
            }

            y == 149 && x in 50..99 && facing == 1 -> {
                newPos = Pos(x = 49, y = 150 + (x - 50))
                newFacing = 2
                // ok
            }

            y == 199 && x in 0..49 && facing == 1 -> {
                newPos = Pos(x = 100 + x, y = 0)
                newFacing = 1
                // ok
            }

            x == 99 && y in 50..99 && facing == 0 -> {
                newPos = Pos(x = 100 + y - 50, y = 49)
                newFacing = 3
                // ok
            }

            y == 0 && x in 50..99 && facing == 3 -> {
                newPos = Pos(x = 0, y = 150 + x - 50)
                newFacing = 0
                // ok
            }

            y == 100 && x in 0..49 && facing == 3 -> {
                newPos = Pos(x = 50, y = 50 + x)
                newFacing = 0
                // ok
            }

            y == 49 && x in 100..149 && facing == 1 -> {
                newPos = Pos(x = 99, y = 50 + x - 100)
                newFacing = 2
                // ok
            }

            x == 49 && y in 150..199 && facing == 0 -> {
                newPos = Pos(x = y - 100, y = 149)
                newFacing = 3
                // ok
            }

            x == 0 && y in 150..199 && facing == 2 -> {
                newPos = Pos(x = 50 + y - 150, y = 0)
                newFacing = 1
                // ok
            }

            x == 50 && y in 50..99 && facing == 2 -> {
                newPos = Pos(x = y - 50, y = 100)
                newFacing = 1
                // ok
            }

            x == 0 && y in 100..149 && facing == 2 -> {
                newPos = Pos(x = 50, y = 49 - (y - 100))
                newFacing = 0
                // ok
            }

            y == 0 && x in 100..149 && facing == 3 -> {
                newPos = Pos(x = x - 100, y = 199)
                newFacing = 3
                // ok
            }

            x == 50 && y in 0..49 && facing == 2 -> {
                newPos = Pos(x = 0, y = 149 - y)
                newFacing = 0
                // ok
            }

            else -> throw IllegalArgumentException("$this $facing")
        }
        return newPos to newFacing
    }
    for (instruction in instructions) {
        when (instruction) {
            is Int -> repeat(instruction) {
                val (newPos, newFacing) = pos.neighborCube(facing)
                val backAgain = newPos.neighborCube((newFacing + 2) % 4)
                assert(backAgain.first == pos) { "($pos, $facing) -> ($newPos, $newFacing) -> $backAgain" }
                if (maze[newPos.y][newPos.x] != '#') {
                    pos = newPos
                    facing = newFacing
                }
            }

            "L" -> facing = (4 + facing - 1) % 4
            "R" -> facing = (facing + 1) % 4
        }
    }
    println(1000 * (pos.y + 1) + 4 * (pos.x + 1) + facing)
}

