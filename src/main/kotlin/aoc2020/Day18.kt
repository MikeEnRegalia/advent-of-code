package aoc2020

import aoc2020.AoC2020Day18.Parser

fun main() = with(generateSequence(::readLine).toList()) {
    sequenceOf(false, true).forEach { part2 -> sumOf { Parser(it, part2).expression() }.also(::println) }
}

object AoC2020Day18 {
    class Parser(val line: String, val part2: Boolean, var pos: Int = 0) {
        fun expression(): Long {
            val r = mutableListOf<Long>()
            while (pos < line.length) when (val c = peek()) {
                ' ' -> read(c)
                '+' -> {
                    read(c)
                    read(' ')
                    r[r.size - 1] = r.last() + term()
                }
                '*' -> {
                    read(c)
                    read(' ')
                    if (part2) r += term() else r[r.size - 1] = r.last() * term()
                }
                ')' -> break
                else -> r += term()
            }
            return r.reduce(Long::times)
        }

        fun term() = if (peek() == '(') parentheses() else literal()

        fun parentheses(): Long {
            read('(')
            val result = expression()
            read(')')
            return result
        }

        fun literal() = buildString { while (peek()?.isDigit() == true) append(read()) }.toLong()

        fun peek() = line.getOrNull(pos)
        fun read(c: Char? = null): Char = peek()
            .also { if (c != null && it != c) throw IllegalStateException(it.toString()) }
            .also { pos++ }!!
    }
}