package y2018.day09

import java.util.*

fun day09(players: Int, marbles: Int): Long = with(Circle()) {
    var currentPlayer = 0
    val scores = mutableMapOf<Int, Long>()

    for (marbleToPlace in 1 until marbles) {
        if (marbleToPlace % 23 > 0) {
            move(2)
            add(marbleToPlace)
            move(-1)
        } else {
            move(-7)
            val other = next()
            scores.compute(currentPlayer) { _, old -> (old ?: 0) + marbleToPlace + other }
            remove()
        }
        currentPlayer = (currentPlayer + 1) % players
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

    fun add(marbleToPlace: Int) = i.add(marbleToPlace)
    fun next(): Int = i.next()
    fun remove() = i.remove()
}