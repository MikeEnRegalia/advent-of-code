package aoc2018.day18

fun day18Settlers(input: String, rounds: Long = 10): Int {
    val initialMap = input.split("\n")
        .mapIndexed { y, row -> row.mapIndexed { x, c -> Pos(x, y) to c.toString() } }
        .flatten().toMap()

    return with(initialMap.evolve(rounds)) { values.count { it == "|" } * values.count { it == "#" } }
}

private fun Map<Pos, String>.evolve(rounds: Long): Map<Pos, String> {
    var map = this
    val history = mutableListOf(map)

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
        val prevRound = history.indexOf(map)
        if (prevRound > -1) {
            val cycle = history.drop(prevRound)
            map = cycle[((rounds - round) % cycle.size).toInt()]
            break
        }
        history += map
    }
    return map
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