package aoc2022

import kotlin.math.abs

// DISCLAIMER: Got the idea of refactoring to do both parts in one run from other solutions.
fun main() {
    data class Pos(val x: Int, val y: Int) {
        fun diagonals() = sequenceOf(Pos(x - 1, y - 1), Pos(x + 1, y + 1), Pos(x - 1, y + 1), Pos(x + 1, y - 1))
        fun straights() = sequenceOf(Pos(x - 1, y), Pos(x + 1, y), Pos(x, y + 1), Pos(x, y - 1))
        fun dist(p: Pos) = abs(x - p.x) + abs(y - p.y)
        fun follow(head: Pos) = when {
            dist(head) >= 3 -> diagonals().minBy { it.dist(head) }
            dist(head) == 2 && head !in diagonals() -> straights().minBy { it.dist(head) }
            else -> this
        }

        fun move(dir: String) = when (dir) {
            "R", "L" -> copy(x = x + if (dir == "R") 1 else -1)
            else -> copy(y = y + if (dir == "U") 1 else -1)
        }
    }

    val rope = MutableList(10) { Pos(0, 0) }
    val H = List(2) { mutableSetOf<Pos>() }

    val commands = generateSequence(::readlnOrNull).map { it.split(" ") }
    for ((dir, n) in commands) repeat(n.toInt()) {
        for (i in rope.indices) rope[i] = when (i) {
            0 -> rope.first().move(dir)
            else -> rope[i].follow(rope[i - 1])
        }

        H[0] += rope[1]
        H[1] += rope.last()
    }

    H.map { it.size }.forEach(::println)
}