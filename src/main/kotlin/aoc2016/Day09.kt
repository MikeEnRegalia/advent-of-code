package aoc2016

fun main() {
    val input = generateSequence(::readLine).joinToString("")
    val decompressed = mutableListOf<Pair<Int, String>>()
    var pos = 0
    while (pos < input.length) {
        val next = input.indexOf('(', pos)
        if (next == -1) {
            decompressed += 1 to input.substring(pos)
            pos = input.length
            continue
        }

        val data = input.substring(pos, next)
        decompressed += 1 to data

        val end = input.indexOf(')', next)
        val (l, x) = input.substring(next + 1, end).split("x").map(String::toInt)
        decompressed += x to input.substring(end + 1, end + 1 + l)
        pos = end + 1 + l
    }
    println(decompressed.sumOf { (x, data) -> x * data.length })
}