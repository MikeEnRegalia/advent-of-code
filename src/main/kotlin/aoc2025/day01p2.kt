package aoc2025

var dial2 = 50

fun main() = println(generateSequence(::readLine).sumOf { line ->
    (0 until line.substring(1).toInt()).count {
        dial2 += if (line.startsWith('R')) 1 else -1
        dial2 % 100 == 0
    }
})
