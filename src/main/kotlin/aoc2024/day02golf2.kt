package aoc2024

fun main() = println(generateSequence(::readLine).count {
    it.split(" ").map(String::toInt)
        .run { takeIf { first() < last() } ?: reversed() }
        .run {
            (0..size)
                .map { filterIndexed { i, _ -> it != i } }
                .any { it.windowed(2).all { it[1] - it[0] in 1..3 } }
        }
})
