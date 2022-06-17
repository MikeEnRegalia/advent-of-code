package aoc2021b

fun main() = runViaStdInOut { day02() }

fun List<String>.day02() = with(toCommands()) { dive(Pos::part1) to dive(Pos::part2) }

private fun List<String>.toCommands() = map { it.split(" ") }.map { it[0] to it[1].toInt() }

private inline fun List<Pair<String, Int>>.dive(move: Pos.(String, Int) -> Unit) =
    fold(Pos()) { p, (cmd, x) -> p.apply { move(cmd, x) } }.let { it.horizontal * it.depth }

private data class Pos(var horizontal: Int = 0, var depth: Int = 0, var aim: Int = 0) {
    fun part1(cmd: String, x: Int) {
        when (cmd) {
            "forward" -> horizontal += x
            "up" -> depth -= x
            "down" -> depth += x
        }
    }

    fun part2(cmd: String, x: Int) {
        when (cmd) {
            "forward" -> {
                horizontal += x; depth += aim * x
            }
            "up" -> aim -= x
            "down" -> aim += x
        }
    }
}