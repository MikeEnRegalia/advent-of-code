package aoc2025

var dial = 50

fun main() = println(generateSequence(::readLine).count {
    dial += it.replace("R", "").replace("L", "-").toInt()
    dial % 100 == 0
})
