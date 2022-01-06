package aoc2016

fun main() {
    val (width, height) = 50 to 6
    val screen = mutableSetOf<Pair<Int, Int>>()
    for (line in generateSequence(::readLine)) when {
        line.startsWith("rect ") -> {
            val (a, b) = line.substringAfter("rect ").split("x").map(String::toInt)
            for (x in 0 until a) for (y in 0 until b) screen += x to y
        }
        line.startsWith("rotate row y=") -> {
            val (row, offset) = line.substringAfter("rotate row y=").split(Regex(" by ")).map(String::toInt)
            with(screen.filter { (_, y) -> y == row }) {
                forEach(screen::remove)
                forEach { (x, y) -> screen += (x + offset) % width to y }
            }
        }
        line.startsWith("rotate column x=") -> {
            val (col, offset) = line.substringAfter("rotate column x=").split(Regex(" by ")).map(String::toInt)
            with(screen.filter { (x) -> x == col }) {
                forEach(screen::remove)
                forEach { (x, y) -> screen += x to (y + offset) % height }
            }
        }
    }

    println(screen.count())

    (0 until height).joinToString("\n") { y ->
        (0 until width).joinToString("") { x -> if (x to y in screen) "*" else " " }
    }.also(::println)
}