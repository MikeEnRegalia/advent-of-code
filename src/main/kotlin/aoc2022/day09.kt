package aoc2022

import kotlin.math.abs

fun main() = day09(String(System.`in`.readAllBytes())).forEach(::println)

private fun day09(input: String): List<Any?> {
    val moves = input.lines().map { it.split(" ").let { it[0] to it[1].toInt() } }

    data class Pos(val x: Int, val y: Int) {
        fun diagonals() = sequenceOf(Pos(x - 1, y - 1), Pos(x + 1, y + 1), Pos(x - 1, y + 1), Pos(x + 1, y - 1))
        fun straights() = sequenceOf(Pos(x - 1, y), Pos(x + 1, y), Pos(x, y + 1), Pos(x, y - 1))
        override fun toString() = "($x,$y)"
    }

    fun Pos.dist(p: Pos) = abs(x - p.x) + abs(y - p.y)

    fun Pos.follow(head: Pos) = when {
        dist(head) >= 3 -> diagonals().minBy { it.dist(head) }
        dist(head) == 2 && head !in diagonals() -> straights().minBy { it.dist(head) }
        else -> this
    }

    fun simulate(knots: Int): Int {
        val rope = MutableList(knots) { Pos(0, 0) }
        val tailHistory = mutableSetOf(rope.first())

        for ((dir, n) in moves) repeat(n) {
            with(rope.last()) {
                rope[rope.lastIndex] = when (dir) {
                    "R" -> copy(x = x + 1)
                    "L" -> copy(x = x - 1)
                    "U" -> copy(y = y - 1)
                    else -> copy(y = y + 1)
                }
            }

            for (i in rope.lastIndex - 1 downTo 0) {
                rope[i] = rope[i].follow(rope[i + 1])
            }

            tailHistory += rope.first()
            println("$dir $n $rope")
        }
        return tailHistory.size
    }

    return listOf(simulate(2), simulate(10))
}

