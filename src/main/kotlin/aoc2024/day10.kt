package aoc2024

import kotlin.math.min

fun main() {
    with(generateSequence(::readLine).map { it.map(Char::digitToInt) }.toList()) {
        data class Location(val x: Int, val y: Int) {
            fun neighbors() = sequenceOf(d(dx = 1), d(dx = -1), d(dy = 1), d(dy = -1)).filter { isNext(it) }
            fun d(dx: Int = 0, dy: Int = 0) = copy(x = x + dx, y = y + dy)
            fun isNext(l: Location) = l.height() == height() + 1
            fun height() = getOrNull(y)?.getOrNull(x) ?: -1
        }

        val hikes = flatMapIndexed { y, l -> l.mapIndexedNotNull { x, c -> c.takeIf { c == 0 }?.let { Location(x, y) } } }
            .fold(mutableSetOf<List<Location>>()) { acc, trailhead ->
                var t = listOf(trailhead)
                val V = mutableSetOf(t)
                val D = mutableMapOf(t to 0)
                while (true) {
                    t.last().neighbors().map { t + it }.forEach {
                        D.compute(it) { _, v -> min(D.getValue(t) + 1, v ?: Int.MAX_VALUE) }
                    }
                    t = D.keys.firstOrNull { it !in V } ?: break
                    if (t.last().height() == 9) acc += t
                    V += t
                }
                acc
            }
        println(hikes.map { listOf(it.first(), it.last()) }.distinct().size)
        println(hikes.size)
    }
}
