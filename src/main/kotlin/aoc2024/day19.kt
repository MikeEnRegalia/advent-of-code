package aoc2024

fun main() {
    val (towels, designs) = with(generateSequence(::readLine).toList()) { first().split(", ") to drop(2) }
    val cache = mutableMapOf("" to 0L)

    fun String.countCombinations(): Long = cache.getOrPut(this) {
        towels.filter(::startsWith).sumOf { if (this == it) 1 else removePrefix(it).countCombinations() }
    }

    with(designs.map(String::countCombinations)) { listOf(count { it > 0 }, sum()).forEach(::println) }
}
