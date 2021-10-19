package aoc2018.day18

fun day18Settlers(input: String, rounds: Long = 10): Int {
    val initialMap = input.split("\n").mapIndexed { y, row -> row.mapIndexed { x, c -> Pos(x, y) to c.toString() } }
        .flatten()
        .toMap()

    fun Map<Pos, String>.result() = with(values) { count { it == "|" } * count { it == "#" } }

    var map = initialMap
    val prevMaps = mutableListOf(map)

    for (round in 1..rounds) {
        val newMap: Map<Pos, String> = map.entries.fold(mutableMapOf()) { newMap, e ->
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
        map = newMap
        val prevRound = prevMaps.indexOf(map)
        if (prevRound > -1) {
            println("cycle detected in round $round (from $prevRound)")
            val cycle = prevMaps.drop(prevRound)
            return cycle[((rounds - round) % cycle.size).toInt()].result()
        }
        prevMaps += map
    }

    return map.result()
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