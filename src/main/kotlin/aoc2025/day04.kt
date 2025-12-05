package aoc2025

fun main() {
    val grid = generateSequence(::readLine)
        .flatMapIndexed { y, line -> line.mapIndexedNotNull { x, char -> if (char == '@') x to y else null } }
        .toMutableSet()

    fun Pair<Int, Int>.isRemovable() = (-1..1).flatMap { dy -> (-1..1).map { first + it to second + dy } }
        .count(grid::contains) <= 4

    val removals = generateSequence {
        grid.filter(Pair<Int, Int>::isRemovable).also(grid::removeAll).size.takeIf { it > 0 }
    }

    with(removals.toList()) {
        println(first())
        println(sum())
    }
}
