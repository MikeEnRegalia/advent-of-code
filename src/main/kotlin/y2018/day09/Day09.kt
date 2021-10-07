package y2018.day09

import java.util.*

fun day09(players: Int, marbles: Int): Long {
    val circle = LinkedList(listOf(0))

    fun MutableListIterator<Int>.move(delta: Int): MutableListIterator<Int> =
        if (delta == 0) this else when {
            delta < 0 -> if (hasPrevious()) apply { previous() } else circle.listIterator(circle.size - 1)
            else -> if (hasNext()) apply { next() } else circle.listIterator().apply { next() }
        }.move(if (delta > 0) delta - 1 else delta + 1)

    var currentPlayer = 0
    val scores = mutableMapOf<Int, Long>()

    var i: MutableListIterator<Int> = circle.listIterator()

    for (marbleToPlace in 1 until marbles) {
        if (marbleToPlace % 23 > 0) {
            i = i.move(2)
            i.add(marbleToPlace)
            i = i.move(-1)
        } else {
            i = i.move(-7)
            val other = i.next()
            scores.compute(currentPlayer) { _, old -> (old ?: 0) + marbleToPlace + other }
            i.remove()
        }
        currentPlayer = (currentPlayer + 1) % players
    }

    return scores.values.maxOf { it }
}