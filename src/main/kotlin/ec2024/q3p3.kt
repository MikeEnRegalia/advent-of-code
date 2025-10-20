package ec2024

import kotlin.math.abs

fun main() {
    data class Point(val x: Int, val y: Int) {
        fun neighbors() = sequenceOf(
            copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1),
            copy(x = x + 1, y = y + 1), copy(x = x + 1, y = y - 1),
            copy(x = x - 1, y = y + 1), copy(x = x - 1, y = y - 1),
        )
    }

    val grid = generateSequence(::readLine)
        .flatMapIndexed { y, row -> row.mapIndexedNotNull { x, value -> if (value == '.') null else Point(x, y) to 0 } }
        .toMap(mutableMapOf())

    var removed = 0

    fun Point.hasLevelNeighbors() = neighbors().all { neighbor ->
        abs(grid.getOrDefault(neighbor, 0) - grid.getOrDefault(this, 0)) == 0
    }

    do {
        var removedThisTurn = 0

        val remove = grid.keys.filter { it.hasLevelNeighbors() }
        for (point in remove) {
            grid[point] = grid.getValue(point) + 1
            removedThisTurn++
        }

        removed += removedThisTurn
    } while (removedThisTurn > 0)

    println(removed)
}
