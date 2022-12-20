package aoc2022

import kotlin.math.abs

fun main() = day20(String(System.`in`.readAllBytes())).forEach(::println)

private fun day20(input: String): List<Any?> {
    class N(val x: Long) {
        override fun toString() = "$x"
    }

    val numbers = input.lines().map { N(it.toLong()) }

    fun MutableList<N>.swap(i: Int, j: Int) {
        val e = this[i]
        this[i] = this[j]
        this[j] = e
    }

    fun MutableList<N>.swapWithNext(i: Int): Int {
        val j = (i + 1) % size
        swap(i, j)
        return j
    }

    fun MutableList<N>.swapWithPrev(i: Int): Int {
        val j = (size + i - 1) % size
        swap(i, j)
        return j
    }

    fun List<N>.mix(times: Int): List<N> {
        val mixed = toMutableList()

        repeat(times) { round ->
            println(round)
            for (n in this) {
                var i = mixed.indexOf(n)
                var move = abs(n.x)
                if (move == 0L) continue
                while (move > size) move = (move % size) + (move / size)
                //println("move $move ($size)")
                if (n.x > 0L) for (c in 1L..move) i = mixed.swapWithNext(i)
                else for (c in 1L..move) i = mixed.swapWithPrev(i)
            }
        }
        return mixed
    }

    fun List<N>.foo(): Long {
        val indexOfZero = indexOf(single { it.x == 0L })
        return listOf(1000, 2000, 3000).map { elementAt((indexOfZero + it) % size) }.sumOf { it.x }
    }

    println(numbers.mix(1).foo())
    println(numbers.map { N(it.x * 811589153)}.mix(10).foo())
    return listOf()
}