package aoc2025

fun main() {
    val grid = generateSequence(::readLine).flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, char -> if (char == '@') x to y else null }
    }.toMutableSet()

    fun liftable() = grid.filter { (px, py) ->
        (-1..1).flatMap { y -> (-1..1).map { px + it to py + y } }.count { it in grid } <= 4
    }.toSet()

    var lifted = 0
    while (true) {
        val liftable = liftable()
        if (lifted == 0) println(liftable.size)

        if (liftable.isEmpty()) break
        grid -= liftable
        lifted += liftable.size
    }

    println(lifted)
}
