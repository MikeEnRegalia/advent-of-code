package aoc2022

fun main() {
    var x = 1
    var cycle = 1
    var part1 = 0
    val screen = Array(6) { BooleanArray(40) }
    fun tick() {
        if (cycle in listOf(20, 60, 100, 140, 180, 220)) part1 += cycle * x
        val row = (cycle - 1) / 40
        val col = (cycle - 1) % 40
        if (col - x in -1..1) screen[row][col] = true
        cycle++
    }
    for (cmd in generateSequence(::readlnOrNull).map { it.split(" ") }) when {
        cmd[0] == "noop" -> tick()
        else -> {
            tick()
            tick()
            x += cmd[1].toInt()
        }
    }
    println(part1)
    println(screen.joinToString("\n") { it.joinToString("") { c -> if (c) "O" else " " } })
}

