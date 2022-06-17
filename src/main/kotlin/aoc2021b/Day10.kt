package aoc2021b

fun main() = day10(generateSequence(::readLine).toList()).forEach(::println)

fun day10(lines: List<String>): Iterable<Any> {
    var part1 = 0
    val part2Scores = mutableListOf<Long>()
    for (line in lines) {
        var lineReduced = line
        while (true) {
            val s = listOf("()", "[]", "{}", "<>").fold(lineReduced) { acc, s -> acc.replace(s, "") }
            if (s == lineReduced) break
            lineReduced = s
        }

        var incomplete: String? = null
        if (lineReduced.isNotEmpty()) {
            incomplete = lineReduced
        }

        while (true) {
            val l3 = listOf("(", "[", "{", "<").fold(lineReduced) { acc, s -> acc.replace(s, "") }
            if (l3 == lineReduced) break
            lineReduced = l3
        }

        if (lineReduced.isNotEmpty()) {
            part1 += when (lineReduced[0]) {
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                '>' -> 25137
                else -> throw IllegalArgumentException()
            }
        } else if (incomplete != null) {
            val score = incomplete.reversed().fold(0L) { acc, c ->
                acc * 5 + when (c) {
                    '(' -> 1
                    '[' -> 2
                    '{' -> 3
                    '<' -> 4
                    else -> throw IllegalArgumentException()
                }
            }
            part2Scores += score
        }
    }
    return listOf(part1, part2Scores.let { it.sorted()[it.size / 2] })
}
