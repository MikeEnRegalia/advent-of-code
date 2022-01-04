package aoc2016

import java.math.BigInteger
import java.security.MessageDigest

fun main() {
    val input = "abbhdwsy"

    fun seq(): Sequence<String> {
        var x = 0
        return generateSequence { "$input$x".md5().also { x++ } }.filter { it.startsWith("00000") }
    }

    val part1 = seq().map { it[5].toString() }.take(8).joinToString("")
    println(part1)

    val part2 = MutableList<String?>(8) { null }.run {
        seq().forEach { hash ->
            hash[5].digitToIntOrNull()?.takeIf { it in indices && get(it) == null }?.let { pos ->
                set(pos, hash[6].toString())
                if (!contains(null)) return@run joinToString("")
            }
        }
    }
    println(part2)
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val bigInt = BigInteger(1, md.digest(toByteArray(Charsets.UTF_8)))
    return String.format("%032x", bigInt)
}