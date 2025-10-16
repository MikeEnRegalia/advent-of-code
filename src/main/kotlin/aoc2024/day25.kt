package aoc2024

fun main() = generateSequence(::readLine).filter(String::isNotBlank)
    .chunked(7)
    .partition { "#" in it.first() }.toList()
    .map { grids -> grids.map { lines -> lines.subList(1, 6).run { indices.map { i -> count { it[i] == '#' } } } } }
    .run { first().sumOf { lock -> last().count { key -> lock.zip(key).none { (a, b) -> a + b > 5 } } } }
    .let(::println)
