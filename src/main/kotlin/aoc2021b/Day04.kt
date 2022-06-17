package aoc2021b

fun main() = runViaStdInOut { day04() }

fun List<String>.day04(): Pair<Int, Int> {
    data class Pos(val x: Int, val y: Int)
    data class Slot(val pos: Pos, val number: Int, var bingo: Boolean = false)
    fun List<Slot>.bingo() = sequence {
        for (i in 0..4) {
            yield((0..4).map { Pos(i, it) })
            yield((0..4).map { Pos(it, i) })
        }
    }.any { straight -> straight.all { pos -> first { it.pos == pos }.bingo } }

    val numbers = first().split(",").map { it.toInt() }
    val boards = drop(1).filterNot { it.isEmpty() }.chunked(5) { lines ->
        lines.flatMapIndexed { y, row ->
            row.trim().split(Regex("""\s+""")).mapIndexed { x, n -> Slot(Pos(x, y), n.toInt()) }
        }
    }

    return sequence {
        for (number in numbers) {
            for (slots in boards.filterNot { it.bingo() }) {
                for (slot in slots.filter { it.number == number }) slot.bingo = true
                if (slots.bingo()) yield(number * slots.filterNot { it.bingo }.sumOf { it.number })
            }
        }
    }.toList().run { first() to last() }
}