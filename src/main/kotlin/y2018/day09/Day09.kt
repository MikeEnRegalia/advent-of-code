package y2018.day09

import java.util.*

fun day09(players: Int, marbles: Int): Long = with(Circle()) {
    var player = 0
    val scores = mutableMapOf<Int, Long>()

    for (marble in 1 until marbles) {
        if (marble % 23 == 0) {
            move(-7)
            scores.upsert(player, 0) { it + marble + remove() }
        } else {
            move(2)
            insert(marble)
        }
        player = (player + 1) % players
    }
    scores.values.maxOf { it }
}

private class Circle {
    private val data = LinkedList(listOf(0))
    private var i = data.listIterator()

    fun move(delta: Int) {
        when {
            delta == 0 -> return
            delta > 0 -> if (i.hasNext()) i.next() else i = data.listIterator(1)
            else -> if (i.hasPrevious()) i.previous() else i = data.listIterator(data.size - 1)
        }
        move(if (delta > 0) delta - 1 else delta + 1)
    }

    fun insert(marbleToPlace: Int) = i.add(marbleToPlace).also { move(-1) }
    fun remove(): Int = i.next().also { i.remove() }
}

fun <K, V> MutableMap<K, V>.upsert(key: K, defaultValue: V, f: (V) -> V): V =
    compute(key) { _, old -> f(old ?: defaultValue) }!!

