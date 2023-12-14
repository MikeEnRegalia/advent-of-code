package aoc2023

fun main() {
    data class Pos(val x: Int, val y: Int)

    val data = generateSequence(::readLine).flatMapIndexed { y, l ->
        l.mapIndexed { x, c -> Pos(x, y) to c }
    }.toMap().toMutableMap()

    val width = data.keys.maxOf { it.x } + 1
    val height = data.keys.maxOf { it.y } + 1

    fun printMap() {
        buildString {
            for (y in 0 until height) {
                for (x in 0 until width) append(data[Pos(x, y)] ?: '.')
                append("\n")
            }
        }.also(::println)
    }

    fun rollNorth() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val c = data[Pos(x, y)]
                if (c == 'O') {
                    val newY = (0 until y).reversed().takeWhile { it >= 0 && data[Pos(x, it)] == '.' }.lastOrNull()
                    if (newY != null) {
                        data[Pos(x, y)] = '.'
                        data[Pos(x, newY)] = 'O'
                    }
                }
            }
        }
    }

    rollNorth()

    val part1 = data.filterValues { it == 'O' }.keys.sumOf {
        height - it.y
    }

    println(part1)
}