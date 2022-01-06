package aoc2016

fun main() {
    val input = generateSequence(::readLine)
    val width = 50
    val height = 6

    data class Pos(val x: Int, val y: Int)

    val screen = mutableMapOf<Pos, Boolean>()

    input.forEach { line ->
        when {
            line.startsWith("rect ") -> {
                val (a, b) = line.substringAfter("rect ").split("x").map(String::toInt)
                for (x in 0 until a) for (y in 0 until b) screen[Pos(x, y)] = true
            }
            line.startsWith("rotate row y=") -> {
                val (row, offset) = line.substringAfter("rotate row y=").split(Regex(" by ")).map(String::toInt)
                val toShift = screen.keys.filter { it.y == row }
                toShift.forEach(screen::remove)
                toShift.map { it.copy(x = (it.x + offset) % width) }.forEach { screen[it] = true }
            }
            line.startsWith("rotate column x=") -> {
                val (col, offset) = line.substringAfter("rotate column x=").split(Regex(" by ")).map(String::toInt)
                val toShift = screen.keys.filter { it.x == col }
                toShift.forEach(screen::remove)
                toShift.map { it.copy(y = (it.y + offset) % height) }.forEach { screen[it] = true }
            }
        }
    }

    println(screen.count { it.value })

    (0 until height).joinToString("\n") { y ->
        (0 until width).joinToString("") { x ->
            if (screen[Pos(x, y)] == true) "*" else " "
        }
    }.also(::println)
}