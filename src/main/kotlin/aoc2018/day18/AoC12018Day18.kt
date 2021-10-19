package aoc2018.day18

internal data class Pos(val x: Int, val y: Int)

fun day18Settlers(input: String, rounds: Long = 10): Int =
    input.split("\n").mapIndexed { y, row -> row.mapIndexed { x, c -> Pos(x, y) to c.toString() } }
        .flatten().toMap()
        .evolve(rounds).values.let { it.countTrees() * it.countLumberyards() }

private fun Iterable<String>.countTrees() = count { it == "|" }
private fun Iterable<String>.countLumberyards() = count { it == "#" }

private fun Map<Pos, String>.evolve(rounds: Long): Map<Pos, String> {
    var map = this
    val history = mutableListOf(map)

    for (round in 1..rounds) {
        map = map.entries.fold(mutableMapOf()) { newMap, (pos, value) ->
            newMap.apply {
                val (trees, lumberyards) = with(map.neighbors(pos)) { countTrees() to countLumberyards() }
                this[pos] = when (value) {
                    "." -> if (trees >= 3) "|" else "."
                    "|" -> if (lumberyards >= 3) "#" else "|"
                    "#" -> if (lumberyards >= 1 && trees >= 1) "#" else "."
                    else -> value
                }
            }
        }

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

internal fun Map<Pos, String>.neighbors(p: Pos) = sequence {
    for (dx in -1..1)
        for (dy in -1..1)
            if (dx != 0 || dy != 0) yield(Pos(p.x + dx, p.y + dy))
}.mapNotNull { this[it] }.toList()