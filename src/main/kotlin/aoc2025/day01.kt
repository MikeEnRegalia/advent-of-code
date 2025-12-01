package aoc2025

fun main() {
    val lines = generateSequence(::readLine).map { it.first() to it.substring(1).toInt() }

    var part1 = 0
    var part2 = 0

    var dial = 50

    for ((dir, distance) in lines) repeat(distance) {
        dial += if (dir == 'R') 1 else -1
        if (dial % 100 == 0) {
            if (it == distance - 1) part1++
            part2++
        }
    }

    println(part1)
    println(part2)
}
