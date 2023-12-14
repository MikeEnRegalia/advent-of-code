package aoc2023

fun main() {
    data class Pos(val x: Int, val y: Int)

    var data = generateSequence(::readLine).flatMapIndexed { y, l ->
        l.mapIndexedNotNull { x, c -> if (c == '.') null else Pos(x, y) to c }
    }.toMap(mutableMapOf())

    val (width, height) = data.keys.maxOf { it.x } + 1 to data.keys.maxOf { it.y } + 1

    fun rollNorth() {
        for (y in 0 until height) for (x in 0 until width) {
            if (data[Pos(x, y)] != 'O') continue
            val y2 = (0 until y).reversed().takeWhile { data[Pos(x, it)] == null }.lastOrNull() ?: continue
            data -= Pos(x, y)
            data[Pos(x, y2)] = 'O'
        }
    }

    fun rotate90() {
        data = data.mapKeysTo(mutableMapOf()) { (k, _) -> Pos(height - 1 - k.y, k.x) }
    }

    fun Map<Pos, Char>.summarize() = filterValues { it == 'O' }.keys.sumOf { height - it.y }

    val history = mutableListOf<Pair<Any, Int>>()

    while (true) {
        repeat(4) {
            rollNorth()
            if (history.isEmpty() && it == 0) println(data.summarize())
            rotate90()
        }

        val key = data.entries.toSet() as Any to data.summarize()
        if (key !in history) history.add(key) else {
            val cycleStart = history.indexOf(key)
            val cycleLength = history.size - cycleStart
            println(history[cycleStart - 1 + (1_000_000_000 - cycleStart) % cycleLength].second)
            return
        }
    }
}