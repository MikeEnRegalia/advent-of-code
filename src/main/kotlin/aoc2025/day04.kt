package aoc2025

fun main() {
    data class Position(val x: Int, val y: Int)

    val grid = generateSequence(::readLine).flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, char -> if (char == '@') Position(x, y) else null }
    }.toSet()

    fun Set<Position>.liftable() = filter { p ->
            (-1..1).flatMap { y -> (-1..1).map { x -> Position(p.x + x, p.y + y) } }
                .count { it != p && it in this } < 4
        }.toSet()

    println(grid.liftable().count())

    val part2 = grid.toMutableSet()
    while (true) {
        val liftable = part2.liftable()
        if (liftable.isEmpty()) break
        part2 -= liftable
    }

    println(grid.size - part2.size)
}
