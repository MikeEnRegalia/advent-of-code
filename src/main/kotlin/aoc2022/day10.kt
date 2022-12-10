package aoc2022

fun main() {
    var x = 1
    var part1 = 0
    val screen = Array(6) { BooleanArray(40) }
    fun String.parseRow() = if (this == "noop") sequenceOf(0) else sequenceOf(0, substringAfterLast(" ").toInt())
    generateSequence(::readlnOrNull).flatMap(String::parseRow).forEachIndexed { cycle, n ->
        (cycle + 1).also { if (it in listOf(20, 60, 100, 140, 180, 220)) part1 += it * x }
        val row = (cycle) / 40
        val col = (cycle) % 40
        if (col - x in -1..1) screen[row][col] = true
        x += n
    }
    println(part1)
    println(screen.joinToString("\n") { it.joinToString("") { c -> if (c) "O" else " " } })
}

