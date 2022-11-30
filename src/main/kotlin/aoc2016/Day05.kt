package aoc2016

import java.math.BigInteger
import java.security.MessageDigest

fun main() {
    val input = "abbhdwsy"

    fun seq() = generateSequence(0) { it + 1 }.map { "$input$it" }
        .map(String::md5).filter { it.startsWith("00000") }

    val part1 = seq().map { it[5].toString() }.take(8).joinToString("")
    println(part1)

    val part2 = MutableList<String?>(8) { null }.run {
        seq().mapNotNull { hash -> hash[5].digitToIntOrNull()?.let { it to hash[6] } }
            .filter { (pos) -> pos in indices && get(pos) == null }
            .forEach { (pos, code) ->
                set(pos, code.toString())
                if (!contains(null)) return@run joinToString("")
            }
    }
    println(part2)
}

private fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return String.format("%032x", BigInteger(1, md.digest(toByteArray(Charsets.UTF_8))))
}