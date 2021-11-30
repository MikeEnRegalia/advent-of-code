package aoc2018.day18

internal data class Pos(val x: Int, val y: Int)
internal typealias Area = Map<Pos, String>

fun day18Settlers(input: String, rounds: Long = 10): Int =
    input.toArea().evolve(rounds).values.let { it.trees() * it.lumberyards() }

private fun String.toArea() =
    split("\n").flatMapIndexed { y, r -> r.mapIndexed { x, c -> Pos(x, y) to c.toString() } }.toMap()

private fun Iterable<String>.trees() = count { it == "|" }
private fun Iterable<String>.lumberyards() = count { it == "#" }

private fun Area.evolve(rounds: Long) = with(mutableListOf(this)) {
    for (round in 1..rounds) last().evolveOnce().let { next ->
        shortcut(rounds, round, next)?.let { return it } ?: add(next)
    }
    last()
}

private fun Area.evolveOnce() =
    entries
        .fold(mutableMapOf<Pos, String>()) { area, (pos, value) ->
            area.apply {
                val (trees, lumberyards) = with(pos.neighbors(this@evolveOnce)) { trees() to lumberyards() }
                this[pos] = when (value) {
                    "." -> if (trees >= 3) "|" else "."
                    "|" -> if (lumberyards >= 3) "#" else "|"
                    "#" -> if (lumberyards >= 1 && trees >= 1) "#" else "."
                    else -> value
                }
            }
        }

private fun MutableList<Area>.shortcut(rounds: Long, round: Long, area: Area) =
    indexOf(area).takeIf { it != -1 }
        ?.let { this[(it + (rounds - round) % (size - it)).toInt()] }

internal fun Pos.neighbors(area: Area) = neighborPositions().mapNotNull { area[it] }.toList()

private fun Pos.neighborPositions() = sequence {
    for (dx in -1..1) for (dy in -1..1) if (dx != 0 || dy != 0) yield(Pos(x + dx, y + dy))
}