package aoc2025

fun main() {
    val lines = generateSequence(::readLine).toList()

    val beams = mutableSetOf(lines.first().indexOf("S"))
    var splits = 0
    for (line in lines.drop(1)) {
        for (i in lines.first().indices) {
            if (line[i] == '^' && i in beams) {
                beams -= i
                beams += i - 1
                beams += i + 1
                splits++
            }
        }
        println(line.mapIndexed { i, c -> if (i in beams) '|' else c }.joinToString(""))
    }

    println(splits)
}
