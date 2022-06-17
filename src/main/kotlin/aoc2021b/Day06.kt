package aoc2021b

fun main() = generateSequence(::readLine).single().split(",").map { it.toInt() }
    .run { LongArray(9) { i -> count { i == it }.toLong() } }
    .day06().forEach(::println)

fun LongArray.day06() = run { listOf(grow(80), grow(256)) }

fun LongArray.grow(days: Int) = copyOf().run {
    repeat(days) {
        val toClone = this[0]
        for (timer in 0..8) this[timer] = when (timer) {
            6 -> this[7] + toClone
            8 -> toClone
            else -> this[timer + 1]
        }
    }
    sum()
}