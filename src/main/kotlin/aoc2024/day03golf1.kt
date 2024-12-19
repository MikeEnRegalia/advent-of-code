package aoc2024

fun main() = println(
    generateSequence(::readLine).joinToString().split("""(mul\(|\))""".toRegex())
        .mapNotNull { it.split(",").takeIf { it.size == 2 } }
        .mapNotNull { it.mapNotNull(String::toIntOrNull).takeIf { it.size == 2 } }
        .sumOf { it[0] * it[1] })
