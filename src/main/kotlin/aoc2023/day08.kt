package aoc2023

fun main() = day08(generateSequence(::readlnOrNull).toList()).forEach(::println)

private fun day08(lines: List<String>): List<Any?> {
    val moves = lines[0]

    data class Foo(val left: String, val right: String)

    val dirs = lines.drop(2).map {
        it.filter { it.isLetter() }.chunked(3).let { data ->
            data[0] to Foo(data[1], data[2])
        }
    }.toMap()

    var curr = "AAA"
    var n = 0
    while (curr != "ZZZ") {
        val move = moves[n % moves.length]
        curr = dirs.getValue(curr).let { if (move == 'L') it.left else it.right }
        n++
    }
    return listOf(n)
}