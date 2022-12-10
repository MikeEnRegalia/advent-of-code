package aoc2022

fun main() = day10(String(System.`in`.readAllBytes())).forEach(::println)

private fun day10(input: String): List<Any?> {
    var x = 1
    var cycle = 1
    val agg = mutableListOf<Int>()
    val commands = input.lines().map { it.split(" ") }
    val screen = List(6) { MutableList(40) { "." } }
    fun count() {
        if (cycle in listOf(20, 60, 100, 140, 180, 220)) {
            agg += cycle * x
        }
        val sprite = x
        val row = (cycle-1) /40
        val col = (cycle-1) % 40
        if (col in (x-1..x+1)) screen[row][col] = "#"
        cycle++
    }

    for (cmd in commands) {
        if (cmd[0] == "noop") {
            count()
            continue
        }
        count()
        count()
        val toAdd = cmd[1].toInt()
        x += toAdd
    }
    return listOf(agg.sum(), screen.joinToString("\n") { it.joinToString("") })
}

