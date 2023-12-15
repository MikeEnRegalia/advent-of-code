package aoc2023

fun main() {

    fun String.hash() = fold(0) { acc, c ->
        (17 * (acc + c.code)) % 256
    }

    val part1 = generateSequence(::readLine).single()
        .split(",")
        .sumOf { it.hash() }

    println(part1)
}