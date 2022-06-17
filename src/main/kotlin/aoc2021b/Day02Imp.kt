package aoc2021b

fun main() {
    runViaStdInOut { day02Imp() }
}

fun <T> List<String>.splitMap(delimiters: String, t: (List<String>) -> T) = map { it.split(delimiters) }.map { t(it) }

fun List<String>.day02Imp() = splitMap(" ") { it[0] to it[1].toInt() }.let { commands ->
    data class Pos(var horizontal: Int = 0, var depth: Int = 0, var aim: Int = 0)

    val part1 = Pos().run {
        for ((command, x) in commands) {
            when (command) {
                "forward" -> horizontal += x
                "up" -> depth -= x
                "down" -> depth += x
            }
        }
        horizontal * depth
    }

    val part2 = Pos().run {
        for ((command, x) in commands) {
            when (command) {
                "forward" -> {
                    horizontal += x; depth += aim * x
                }

                "up" -> aim -= x
                "down" -> aim += x
            }
        }
        horizontal * depth
    }

    part1 to part2
}