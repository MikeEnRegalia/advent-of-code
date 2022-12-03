package aoc2022

fun main() = day03(String(System.`in`.readAllBytes())).forEach(::println)

fun day03(input: String): List<Any?> {
    fun Char.score() = this - if (isLowerCase()) 'a' - 1 else 'A' - 27

    with(input.lines()) {
        val part1 = sumOf { line ->
            val (a, b) = line.chunked(line.length / 2)
            line.toSet().filter { it in a && it in b }.sumOf(Char::score)
        }
        val part2 = chunked(3).sumOf { group ->
            group.flatMapTo(mutableSetOf(), String::toList).single { c -> group.all { c in it } }.score()
        }
        return listOf(part1, part2)
    }
}
