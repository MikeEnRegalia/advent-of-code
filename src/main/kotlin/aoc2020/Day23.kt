package aoc2020

import java.util.LinkedList

fun main() {
    val circle = "925176834".mapTo(ArrayDeque(), Char::digitToInt)

    fun part1() {
        repeat(100) {
            val pickedUp = LinkedList<Int>().apply {
                add(circle.removeAt(1))
                add(circle.removeAt(1))
                add(circle.removeAt(1))
            }

            var cup = circle.first() - 1
            while (pickedUp.contains(cup) || cup < 1) cup = if (cup <= 1) circle.maxOf { it } else cup - 1

            circle.addAll(circle.indexOf(cup) + 1, pickedUp)
            circle.addLast(circle.removeAt(0))
        }

        val one = circle.indexOf(1)
        (1 until circle.size)
            .map { circle[(one + it) % circle.size] }
            .joinToString("") { it.toString() }
            .also(::println)
    }
    part1()

    fun part2(smallCircle: List<Int>) {
        var x = smallCircle.maxOf { it }
        val init = buildList {
            addAll(smallCircle)
            while (size < 1_000_000) {
                x++
                add(x)
            }
        }

        var circle = init.toIntArray()

        repeat(10_000_000) { turn ->
            if (turn % 10000 == 0) println(turn)

            var cup = circle.first() - 1
            while (circle[1] == cup || circle[2] == cup || circle[3] == cup || cup < 1)
                cup = if (cup <= 1) x else cup - 1

            val destinationCupIndex = (circle.indexOf(cup) + 1) % circle.size

            println(destinationCupIndex)
            circle = IntArray(1_000_000).apply {
                if (destinationCupIndex == 0) {
                    circle.copyInto(this, 0, 4, circle.size)
                    this[circle.size - 4] = cup
                    circle.copyInto(this, circle.size - 4 + 1, 1, 4)
                } else {
                    circle.copyInto(this, 0, 4, destinationCupIndex + 1)
                    circle.copyInto(this, destinationCupIndex + 1 - 4, 1, 4)
                    circle.copyInto(this, destinationCupIndex + 1, destinationCupIndex + 1, circle.size)
                }
                this[size - 1] = circle[0]
            }
            assert(circle.distinct().size == circle.size)
        }

        val one = circle.indexOf(1)
        println(circle[one + 1] * circle[one + 2])
    }

    part2(circle)
}
