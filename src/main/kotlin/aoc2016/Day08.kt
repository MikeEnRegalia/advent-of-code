package aoc2016

fun main() {
    val (width, height) = 50 to 6
    val screen = mutableMapOf<Pair<Int, Int>, Boolean>()
    for (line in generateSequence(::readLine)) when {
        line.startsWith("rect ") -> {
            val (a, b) = line.substringAfter("rect ").split("x").map(String::toInt)
            for (x in 0 until a) for (y in 0 until b) screen[Pair(x, y)] = true
        }
        line.startsWith("rotate row y=") -> {
            val (row, offset) = line.substringAfter("rotate row y=").split(Regex(" by ")).map(String::toInt)
            with(screen.keys.filter { (_, y) -> y == row }) {
                forEach(screen::remove)
                forEach { (x, y) -> screen[Pair((x + offset) % width, y)] = true }
            }
        }
        line.startsWith("rotate column x=") -> {
            val (col, offset) = line.substringAfter("rotate column x=").split(Regex(" by ")).map(String::toInt)
            with(screen.keys.filter { (x) -> x == col }) {
                forEach(screen::remove)
                forEach { (x, y) -> screen[Pair(x, (y + offset) % height)] = true }
            }
        }
    }

    println(screen.count { it.value })

    (0 until height).joinToString("\n") { y ->
        (0 until width).joinToString("") { x -> if (screen[Pair(x, y)] == true) "*" else " " }
    }.also(::println)
}