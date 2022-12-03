package aoc2022

fun main() = day03(String(System.`in`.readAllBytes())).forEach(::println)

fun day03(input: String) = with(input.lines()) {
    listOf(
        sumOf { l -> l.toSet().filter { l.chunked(l.length / 2).contain(it) }.sumOf(Char::score) },
        chunked(3).sumOf { g -> g.flatMapTo(mutableSetOf(), String::toSet).single(g::contain).score() }
    )
}

private fun Char.score() = this - if (isLowerCase()) 'a' - 1 else 'A' - 27
private fun List<CharSequence>.contain(c: Char) = all { c in it }
