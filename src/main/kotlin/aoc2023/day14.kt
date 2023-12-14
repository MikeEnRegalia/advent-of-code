package aoc2023

fun main() {
    data class Pos(val x: Int, val y: Int)

    var data = generateSequence(::readLine).flatMapIndexed { y, l ->
        l.mapIndexedNotNull { x, c -> if (c == '.') null else Pos(x, y) to c }
    }.toMap(mutableMapOf())

    val width = data.keys.maxOf { it.x } + 1
    val height = data.keys.maxOf { it.y } + 1

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

        val toCache = data.entries.toSet() to data.summarize()
        if (toCache !in history) history.add(toCache) else {
            val cycleStart = history.indexOf(toCache)
            var pos = 0
            for (i in 0..<1_000_000_000 - 1) {
                pos = if (pos == history.indices.last) cycleStart else pos + 1
            }
            println(history[pos].second)
            return
        }
    }
}