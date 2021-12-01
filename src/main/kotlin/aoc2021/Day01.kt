package aoc2021

fun main() {
    with(generateSequence(::readLine).map { it.toInt() }.toList()) {
        println(countIncreases())
        println(aggregate().countIncreases())
    }
}

private fun List<Int>.countIncreases() =
    (1 until size).count { this[it] > this[it - 1] }

private fun List<Int>.aggregate() =
    (2 until size).map { this[it - 2] + this[it - 1] + this[it] }
