package aoc2016

fun main() = with(generateSequence(::readLine).joinToString("")) { listOf(expand(), expand(true)).forEach(::println) }

private fun String.expand(rec: Boolean = false): Long {
    var expanded = 0L
    var pos = 0
    while (true) {
        val next = indexOf('(', pos).takeIf { it != -1 } ?: return expanded + (length - pos)
        expanded += next - pos

        val start = indexOf(')', next) + 1
        val (l, x) = substring(next + 1, start - 1).split("x").let { it[0].toInt() to it[1].toLong() }
        expanded += x * substring(start, start + l).run { if (rec) expand(true) else length }.toLong()
        pos = start + l
    }
}