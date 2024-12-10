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

        flatMapIndexed { y, l -> l.mapIndexedNotNull { x, c -> c.takeIf { c == 0 }?.let { Location(x, y) } } }
            .fold(0 to 0) { (part1, part2), head ->
                val trails = mutableSetOf<List<Location>>()
                var t = listOf(head)
                val V = mutableSetOf(t)
                val D = mutableMapOf(t to 0)
                while (true) {
                    t.last().neighbors().map { t + it }.forEach {
                        D.compute(it) { _, v -> min(D.getValue(t) + 1, v ?: Int.MAX_VALUE) }
                    }
                    t = D.keys.firstOrNull { it !in V } ?: break
                    if (t.last().height() == 9) trails += t
                    V += t
                }
                part1 + trails.map { it.last() }.distinct().size to part2 + trails.size
            }.toList().forEach(::println)
    }
}
