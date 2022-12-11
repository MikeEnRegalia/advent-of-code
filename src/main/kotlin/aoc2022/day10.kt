package aoc2022

fun main() {
    val screen = Array(6) { BooleanArray(40) }
    fun String.toRow() = if (this == "noop") sequenceOf(0) else sequenceOf(0, substringAfterLast(" ").toInt())
    generateSequence(::readlnOrNull).flatMap(String::toRow).foldIndexed(1 to 0) { cycle, (x, p1), n ->
        val row = (cycle) / 40
        val col = (cycle) % 40
        if (col - x in -1..1) screen[row][col] = true
        x + n to (cycle + 1).let { if (it in listOf(20, 60, 100, 140, 180, 220)) p1 + it * (x + n) else p1 }
    }.let { (_, part1) -> println(part1) }
    println(screen.joinToString("\n") { it.joinToString("") { c -> if (c) "O" else " " } })
}

