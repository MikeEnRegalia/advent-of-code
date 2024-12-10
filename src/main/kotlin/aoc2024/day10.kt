package aoc2024

import kotlin.math.min

fun main() = with(generateSequence(::readLine).map { it.map(Char::digitToInt) }.toList()) {
    data class Loc(val x: Int, val y: Int) {
        fun d(dx: Int = 0, dy: Int = 0) = copy(x = x + dx, y = y + dy)
        fun height() = getOrNull(y)?.getOrNull(x)
    }

    fun Loc.hikes() = buildSet {
        var t = listOf(this@hikes)
        val (V, D) = mutableSetOf(t) to mutableMapOf(t to 0)
        while (true) {
            with(t.last()) {
                sequenceOf(d(dx = 1), d(dx = -1), d(dy = 1), d(dy = -1))
                    .filter { it.height() == (height() ?: 0) + 1 }
                    .map { t + it }
                    .forEach { D.compute(it) { _, v -> min(D.getValue(t) + 1, v ?: Int.MAX_VALUE) } }
            }
            t = D.keys.firstOrNull { it !in V } ?: break
            if (t.last().height() == 9) add(t)
            V += t
        }
    }

    val hikes = flatMapIndexed { y, l ->
        l.mapIndexedNotNull { x, c -> c.takeIf { c == 0 }?.let { Loc(x, y).hikes() } }
    }.reduce(Set<List<Loc>>::union)

    sequenceOf(hikes.map { listOf(it.first(), it.last()) }.distinct(), hikes).forEach { println(it.size) }
}
