package aoc2018.day18

fun day18Settlers(input: String): Int {
    val initialMap = input.split("\n").mapIndexed { y, row -> row.mapIndexed { x, c -> Pos(x, y) to c.toString() } }
        .flatten()
        .toMap()

    var map = initialMap
    for (round in 1..10) {
        map = map.entries.fold(mutableMapOf()) { newMap, e ->
            val adjacent = e.key.adjacent().mapNotNull { map[it] }.groupingBy { it }.eachCount()
            val adjacentTrees = adjacent.getOrDefault("|", 0)
            val adjacentLumberyards = adjacent.getOrDefault("#", 0)
            newMap[e.key] = when (e.value) {
                "." -> if (adjacentTrees >= 3) "|" else "."
                "|" -> if (adjacentLumberyards >= 3) "#" else "|"
                "#" -> if (adjacentLumberyards >= 1 && adjacentTrees >= 1) "#" else "."
                else -> e.value
            }
            newMap
        }
    }

    return with(map.values) { count { it == "|" } * count { it == "#" } }
}

internal data class Pos(val x: Int, val y: Int) {
    fun adjacent() = sequence {
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (dx != 0 || dy != 0) yield(Pos(x + dx, y + dy))
            }
        }
    }
}