package aoc2024

fun main() {
    val (towels, patterns) = with(generateSequence(::readLine).toList()) { first().split(", ") to drop(2) }

    val cache = mutableMapOf<String, Long>()

    fun String.match(): Long = cache.getOrPut(this) {
        towels.sumOf { towel ->
            when {
                !startsWith(towel) -> 0L
                length == towel.length -> 1L
                else -> drop(towel.length).match()
            }
        }
    }

    println(patterns.map(String::match).count { it > 0 })
}
