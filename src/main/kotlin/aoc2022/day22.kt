package aoc2022

fun main() {
    val lines = generateSequence(::readlnOrNull).toList()
    val mazeLines = lines.dropLast(2)
    val maze = mazeLines.map { line ->
        CharArray(mazeLines.maxOf { it.length }) { i -> if (i > line.lastIndex) ' ' else line[i] }
    }.toTypedArray()
    val width = maze[0].size
    val height = maze.size

    val instructions: List<Any> = lines.last().fold(mutableListOf<String>()) { acc, c ->
        if (c.isDigit()) {
            if (acc.isEmpty() || acc.last().toIntOrNull() == null) acc.add("")
            acc[acc.lastIndex] = acc.last() + c
        }
        else acc.add(c.toString())
        acc
    }.map { it.toIntOrNull() ?: it }

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

        fun rightOrWrap() =
            if (x + 1 < width && maze[y][x + 1] != ' ') copy(x = x + 1)
            else copy(x = maze[y].indexOfFirst { it != ' ' })
        fun downOrWrap() =
            if (y + 1 < height && maze[y + 1][x] != ' ') copy(y = y + 1)
            else copy(y = maze.indexOfFirst { it[x] != ' ' })
        fun leftOrWrap() =
            if (x - 1 >= 0 && maze[y][x - 1] != ' ') copy(x = x - 1)
            else copy(x = maze[y].indexOfLast { it != ' ' })
        fun upOrWrap() =
            if (y - 1 >= 0 && maze[y - 1][x] != ' ') copy(y = y - 1)
            else copy(y = maze.indexOfLast { it[x] != ' ' })
    }

    var pos = Pos(maze[0].indexOf('.'), 0)
    var facing = 0
    for (instruction in instructions) {
        println("$pos $facing (${pos.neighbors()})")
        when (instruction) {
            is Int -> repeat(instruction) { pos = pos.neighbors()[facing] }
            "L" -> facing = (4 + facing - 1) % 4
            "R" -> facing = (facing + 1) % 4
        }
    }
    println(1000 * (pos.y + 1) + 4 * (pos.x + 1) + facing)
}

