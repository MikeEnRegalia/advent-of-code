package aoc2016

fun main() = with(generateSequence(::readLine).joinToString("")) { listOf(expand(), expand(true)).forEach(::println) }

private fun String.expand(rec: Boolean = false): Long {
    var expanded = 0L
    var pos = 0
    while (true) {
        val marker = indexOf('(', pos).takeIf { it != -1 } ?: return expanded + (length - pos)
        expanded += marker - pos

        val block = indexOf(')', marker) + 1
        val (len, x) = substring(marker + 1, block - 1).split("x").let { it[0].toInt() to it[1].toLong() }
        expanded += x * substring(block, block + len).run { if (rec) expand(true) else length }.toLong()
        pos = block + len
    }
}