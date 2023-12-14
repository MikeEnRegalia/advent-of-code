package aoc2023

fun main() {
    data class Pos(val x: Int, val y: Int)

    val data = generateSequence(::readLine).flatMapIndexed { y, l ->
        l.mapIndexedNotNull { x, c -> if (c == '.') null else Pos(x, y) to c }
    }.toMap().toMutableMap()

    val width = data.keys.maxOf { it.x } + 1
    val height = data.keys.maxOf { it.y } + 1

    fun Map<Pos, Char>.asString() = buildString {
        for (y in 0 until height) {
            for (x in 0 until width) append(this@asString[Pos(x, y)] ?: '.')
            append("\n")
        }
        append("\n")
    }

    fun rollNorth() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val c = data[Pos(x, y)]
                if (c == 'O') {
                    val newY = (0 until y).reversed().takeWhile { data[Pos(x, it)] == null }.lastOrNull()
                    if (newY != null) {
                        data -= Pos(x, y)
                        data[Pos(x, newY)] = 'O'
                    }
                }
            }
        }
    }

    fun rollSouth() {
        for (y in (0 until height).reversed()) {
            for (x in 0 until width) {
                val c = data[Pos(x, y)]
                if (c == 'O') {
                    val newY = (y + 1 until height).takeWhile { data[Pos(x, it)] == null }.lastOrNull()
                    if (newY != null) {
                        data -= Pos(x, y)
                        data[Pos(x, newY)] = 'O'
                    }
                }
            }
        }
    }

    fun rollWest() {
        for (x in 0 until width) {
            for (y in 0 until height) {
                val c = data[Pos(x, y)]
                if (c == 'O') {
                    val newX = (0 until x).reversed().takeWhile { data[Pos(it, y)] == null }.lastOrNull()
                    if (newX != null) {
                        data -= Pos(x, y)
                        data[Pos(newX, y)] = 'O'
                    }
                }
            }
        }
    }

    fun rollEast() {
        for (y in 0 until height) {
            for (x in (0 until width).reversed()) {
                val c = data[Pos(x, y)]
                if (c == 'O') {
                    val newX = (x + 1 until width).takeWhile { data[Pos(it, y)] == null }.lastOrNull()
                    if (newX != null) {
                        data -= Pos(x, y)
                        data[Pos(newX, y)] = 'O'
                    }
                }
            }
        }
    }

    fun Map<Pos, Char>.summarize() = filterValues { it == 'O' }.keys.sumOf { height - it.y }

    val history = mutableListOf<Pair<String, Int>>()

    while (true) {
        rollNorth()

        if (history.isEmpty()) println(data.summarize())

        rollWest()
        rollSouth()
        rollEast()

        val toCache = data.asString() to data.summarize()
        if (toCache in history) {
            val cycleStart = history.indexOf(toCache)
            var pos = -1
            for (i in 1..1_000_000_000) {
                pos = if (pos == history.indices.last) cycleStart else (pos + 1)
            }
            println(history[pos].second)
            return
        }
        history.add(toCache)
    }
}