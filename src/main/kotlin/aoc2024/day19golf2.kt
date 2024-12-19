package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    val cache = mutableMapOf("" to 0L)

    fun String.countCombinations(): Long = cache.getOrPut(this) {
        lines[0].split(", ").filter(::startsWith).sumOf { if (this == it) 1 else removePrefix(it).countCombinations() }
    }

    println(lines.drop(2).sumOf(String::countCombinations))
}
