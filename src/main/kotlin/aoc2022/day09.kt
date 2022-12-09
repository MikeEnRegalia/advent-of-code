package aoc2022

import kotlin.math.abs

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

    fun List<Pair<String, Int>>.walk(knots: Int): Int {
        val rope = MutableList(knots) { Pos(0, 0) }
        val tailHistory = mutableSetOf(rope.last())

        for ((dir, n) in this) repeat(n) {
            rope[0] = rope.first().move(dir)
            for (i in rope.indices.drop(1)) rope[i] = rope[i].follow(rope[i - 1])
            tailHistory += rope.last()
        }
        return tailHistory.size
    }

    with(generateSequence { readlnOrNull() }.toList().map { it.split(" ").let { (a, b) -> a to b.toInt() } }) {
        listOf(2, 10).forEach { println(walk(it)) }
    }
}