package aoc2021b

fun main() {
    val commands = generateSequence(::readLine).map { it.split(" ") }.toList()
    dive(commands, Pos::part1)
    dive(commands, Pos::part2)
}

private inline fun dive(commands: List<List<String>>, move: Pos.(String, Int) -> Pos) = commands
    .fold(Pos()) { p, (cmd, x) -> p.move(cmd, x.toInt()) }
    .also { println(it.horizontal * it.depth) }

private data class Pos(var horizontal: Int = 0, var depth: Int = 0, var aim: Int = 0) {
    fun part1(cmd: String, x: Int): Pos {
        when (cmd) {
            "forward" -> horizontal += x
            "up" -> depth -= x
            "down" -> depth += x
        }
        return this
    }

    fun part2(cmd: String, x: Int): Pos {
        when (cmd) {
            "forward" -> {
                horizontal += x
                depth += aim * x
            }

            "up" -> aim -= x
            "down" -> aim += x
        }
        return this
    }
}