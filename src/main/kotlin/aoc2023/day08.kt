package aoc2023

fun main() {
    val lines = generateSequence(::readlnOrNull).toList()
    val moves = lines[0]
    val paths = lines.drop(2).associate {
        it.filter(Char::isLetter).chunked(3).let { data ->
            data[0] to Pair(data[1], data[2])
        }
    }

    fun part1(start: String, predicate: (String) -> Boolean): Long {
        var curr = start
        var n = 0
        while (!predicate(curr)) {
            val (left, right) = paths.getValue(curr)
            curr = if (moves[n % moves.length] == 'L') left else right
            n++
        }
        return n.toLong()
    }
    listOf(
        part1("AAA") { it == "ZZZ" },
        paths.keys.filter { it.endsWith("A") }
            .map { part1(it) { it.endsWith("Z") } }
            .reduce(::findLCM)).forEach(::println)
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) return lcm
        lcm += larger
    }
    return maxLcm
}