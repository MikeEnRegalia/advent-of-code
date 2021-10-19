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
        map = map.evolveOneRound()
        history.shortcut(rounds, round, map)?.let { return it } ?: history += map
    }
    return map
}

private fun Map<Pos, String>.evolveOneRound() = entries.fold(mutableMapOf<Pos, String>()) { newMap, (pos, value) ->
    newMap.apply {
        val (trees, lumberyards) = with(pos.neighbors(this@evolveOneRound)) { countTrees() to countLumberyards() }
        this[pos] = when (value) {
            "." -> if (trees >= 3) "|" else "."
            "|" -> if (lumberyards >= 3) "#" else "|"
            "#" -> if (lumberyards >= 1 && trees >= 1) "#" else "."
            else -> value
        }
    }
}

private fun List<Map<Pos, String>>.shortcut(rounds: Long, round: Long, map: Map<Pos, String>): Map<Pos, String>? {
    val prevRound = indexOf(map)
    if (prevRound == -1) return null

    val toGo = rounds - round
    val cycleLength = size - prevRound
    return this[(prevRound + toGo % cycleLength).toInt()]
}

internal fun Pos.neighbors(map: Map<Pos, String>) = neighborPositions().mapNotNull { map[it] }.toList()

private fun Pos.neighborPositions() = sequence {
    for (dx in -1..1) for (dy in -1..1) if (dx != 0 || dy != 0) yield(Pos(x + dx, y + dy))
}