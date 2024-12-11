package aoc2024

fun main() = println(generateSequence(::readLine).count {
    it.split(" ").map(String::toInt).run {
        if (first() < last()) this else reversed()
    }.windowed(2).all { it[1] - it[0] in 1..3 }
})
