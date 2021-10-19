package aoc2018.day18

fun day18Settlers(input: String, rounds: Long = 10): Int {
    val initialMap = input.split("\n")
        .mapIndexed { y, row -> row.mapIndexed { x, c -> Pos(x, y) to c.toString() } }
        .flatten().toMap()

    var map = initialMap
    val prevMaps = mutableListOf(map)

    for (round in 1..rounds) {
        val newMap: Map<Pos, String> = map.entries.fold(mutableMapOf()) { newMap, e ->
            val (trees, lumberyards) = with(e.key.adjacent().mapNotNull { map[it] }.groupingBy { it }.eachCount()) {
                getOrDefault("|", 0) to getOrDefault("#", 0)
            }
            newMap[e.key] = when (e.value) {
                "." -> if (trees >= 3) "|" else "."
                "|" -> if (lumberyards >= 3) "#" else "|"
                "#" -> if (lumberyards >= 1 && trees >= 1) "#" else "."
                else -> e.value
            }
            newMap
        }
        map = newMap
        val prevRound = prevMaps.indexOf(map)
        if (prevRound > -1) {
            println("cycle detected in round $round (from $prevRound)")
            val cycle = prevMaps.drop(prevRound)
            map = cycle[((rounds - round) % cycle.size).toInt()]
            break
        }
        prevMaps += map
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