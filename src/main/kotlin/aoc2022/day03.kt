package aoc2022

fun main() = day03(String(System.`in`.readAllBytes())).forEach(::println)

fun day03(input: String): List<Any?> {
    fun Char.score() = this - if (isLowerCase()) 'a' - 1 else 'A' - 27

    return with(input.lines()) {
        listOf(
            sumOf { line ->
                line.toSet().filter { c -> line.chunked(line.length / 2).all { c in it } }.sumOf(Char::score)
            },
            chunked(3).sumOf { group ->
                group.flatMapTo(mutableSetOf(), String::toSet).single { c -> group.all { c in it } }.score()
            }
        )
    }
}
