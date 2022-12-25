package aoc2022

fun main() {
    fun String.fromSNAFU() = reversed().map { c ->
        when (c) {
            '-' -> -1
            '=' -> -2
            else -> c.digitToInt()
        }
    }.foldIndexed(0.toBigInteger()) { i, acc, n -> acc + 5.toBigInteger().pow(i) * n.toBigInteger() }
        .toLong()

    fun Long.toSnafu(): String {
        var carry = 0
        return toString(5).reversed().fold("") { acc, c ->
            var n = c.digitToInt()
            if (carry > 0) {
                n += carry
                carry = 0
            }
            if (n >= 3) carry = 1
            acc + when (n) {
                3 -> "="
                4 -> "-"
                5 -> "0"
                else -> n.toString()
            }
        }.let { if (carry > 0) "$it$carry" else it }.reversed()
    }

    println(generateSequence(::readlnOrNull).sumOf { it.fromSNAFU() }.toSnafu())
}

