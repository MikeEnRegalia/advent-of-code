package aoc2025

var dial = 50

fun main() = println(generateSequence(::readLine).count {
    it.substring(1).toInt().let { n ->
        dial += if (it[0] == 'R') n else -n
        dial % 100 == 0
    }
})
