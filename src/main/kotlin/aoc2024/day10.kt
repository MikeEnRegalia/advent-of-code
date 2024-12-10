package aoc2024

import kotlin.math.min

fun main() = generateSequence(::readLine).map { it.map(Char::digitToInt) }.toList().let { grid ->
    data class Loc(val x: Int, val y: Int)

    fun Loc.height() = grid.getOrNull(y)?.getOrNull(x)

    fun List<Loc>.nextStep(dx: Int = 0, dy: Int = 0) = last().copy(x = last().x + dx, y = last().y + dy)
        .takeIf { it.height() == (last().height() ?: 0) + 1 }?.let { this + it }

    fun List<Loc>.nextSteps() = listOfNotNull(nextStep(dx = 1), nextStep(dx = -1), nextStep(dy = 1), nextStep(dy = -1))

    fun hikesFrom(head: Loc) = buildSet {
        var t = listOf(head)
        val (V, D) = mutableSetOf(t) to mutableMapOf(t to 0)
        while (true) {
            t.nextSteps().forEach { D.compute(it) { _, v -> min(D.getValue(t) + 1, v ?: Int.MAX_VALUE) } }
            t = D.keys.firstOrNull { it !in V } ?: break
            if (t.last().height() == 9) add(t)
            V += t
        }
    }

    val hikes = grid.flatMapIndexed { y, l ->
        l.mapIndexedNotNull { x, c -> c.takeIf { c == 0 }?.let { hikesFrom(Loc(x, y)) } }
    }.reduce(Set<List<Loc>>::union)

    sequenceOf(hikes.map { listOf(it.first(), it.last()) }.distinct(), hikes).forEach { println(it.size) }
}
