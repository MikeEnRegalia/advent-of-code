package aoc2025

fun main() {
    val lines = generateSequence(::readLine).toList()

    val firstLine = lines.first()

    var splits = 0
    val beams = Array(firstLine.length) { i -> if (i == firstLine.indexOf("S")) 1L else 0L }

    for (line in lines) for (i in firstLine.indices) {
        if (line[i] != '^' || beams[i] == 0L) continue
        splits++
        val n = beams[i]
        beams[i] = 0
        beams[i - 1] += n
        beams[i + 1] += n
    }

    println(splits)
    println(beams.sum())
}
