package y2018.day09

import java.util.*

fun day09(players: Int, marbles: Int): Long {
    val circle = LinkedList(listOf(0))
    var currentPlayer = 0
    val scores = mutableMapOf<Int, Long>()

    var i = circle.listIterator(circle.size)

    for (marbleToPlace in 1 until marbles) {
        if (marbleToPlace % 23 == 0) {
            repeat(8) { if (i.hasPrevious()) i.previous() else i = circle.listIterator(circle.size - 1) }
            val other = i.next()
            scores.compute(currentPlayer) { _, old -> (old ?: 0) + marbleToPlace + other }
            i.remove()
            i.next()
        } else {
            if (i.hasNext()) i.next() else i = circle.listIterator().apply { next() }
            i.add(marbleToPlace)
        }
        currentPlayer = (currentPlayer + 1) % players
    }

    return scores.values.maxOf { it }
}