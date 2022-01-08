package aoc2016

fun main() {
    fun String.decompress(rec: Boolean = false): List<Pair<Long, String>> {
        val decompressed = mutableListOf<Pair<Long, String>>()
        var pos = 0
        while (pos < length) {
            val next = indexOf('(', pos)
            if (next == -1) {
                decompressed += 1L to substring(pos)
                pos = length
                continue
            }

            val data = substring(pos, next)
            decompressed += 1L to data

            val end = indexOf(')', next)
            val (l, x) = substring(next + 1, end).split("x").map(String::toInt)
            val block = substring(end + 1, end + 1 + l)
            if (rec) decompressed += block.decompress(rec = true).map { (x2, data) -> x.toLong() * x2 to data }
            else decompressed += x.toLong() to block
            pos = end + 1 + l
        }
        return decompressed
    }

    with(generateSequence(::readLine).joinToString("")) {
        sequenceOf(decompress(), decompress(true)).map { it.sumOf { (x, data) -> x * data.length } }.also(::println)
    }
}