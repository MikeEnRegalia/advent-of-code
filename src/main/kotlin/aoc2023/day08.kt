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

    fun part1(start: String, predicate: (String) -> Boolean): Int {
        var curr = start
        var n = 0
        while (!predicate(curr)) {
            val move = moves[n % moves.length]
            curr = dirs.getValue(curr).let { if (move == 'L') it.left else it.right }
            n++
        }
        return n
    }

    return listOf(
        part1("AAA") { it == "ZZZ" },
        dirs.keys.filter { it.endsWith("A") }
            .map { part1(it) { it.endsWith("Z") } }
            .map(Int::toLong)
            .reduce(::findLCM))
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}