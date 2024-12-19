package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    val cache = mutableMapOf("" to false)

    fun String.matchesDesign(): Boolean = cache.getOrPut(this) {
        lines[0].split(", ").filter(::startsWith).any { this == it || removePrefix(it).matchesDesign() }
    }

    println(lines.drop(2).count(String::matchesDesign))
}
