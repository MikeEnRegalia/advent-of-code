package aoc2018.day18

internal data class Pos(val x: Int, val y: Int)

fun day18Settlers(input: String, rounds: Long = 10): Int =
    input.split("\n")
        .mapIndexed { y, row -> row.mapIndexed { x, c -> Pos(x, y) to c.toString() } }
        .flatten().toMap()
        .evolve(rounds).values.let { values -> values.count { it == "|" } * values.count { it == "#" } }

private fun Map<Pos, String>.evolve(rounds: Long): Map<Pos, String> {
    var map = this
    val history = mutableListOf(map)

    for (round in 1..rounds) {
        val newMap: Map<Pos, String> = map.entries.fold(mutableMapOf()) { newMap, e ->
            val (trees, lumberyards) = with(sequence {
                for (dx in -1..1) {
                    for (dy in -1..1) {
                        if (dx != 0 || dy != 0) yield(Pos(e.key.x + dx, e.key.y + dy))
                    }
                }
            }.mapNotNull { map[it] }.groupingBy { it }.eachCount()) {
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
            val toGo = rounds - round
            val cycleLength = history.size - prevRound
            return history[(prevRound + toGo % cycleLength).toInt()]
        }

        history += map
    }
    return map
}