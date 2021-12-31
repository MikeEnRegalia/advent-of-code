package aoc2020

import aoc2020.AoC2020Day18.Parser

fun main() {
    Parser("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", proper = true).expression().also { println(it) }
    with(generateSequence(::readLine).toList()) {
        sumOf { Parser(it, proper = false).expression() }.also { println(it) }
        sumOf { Parser(it, proper = true).expression() }.also { println(it) }
    }
}

object AoC2020Day18 {
    class Parser(val line: String, var pos: Int = 0, val proper: Boolean) {

        fun expression(parentheses: Boolean = false): Long {
            if (parentheses) read()
            val r = mutableListOf<Long>()
            while (pos != line.length) {
                val c = peek() ?: break
                when {
                    c.isDigit() -> r += literal()
                    c == '(' -> r += expression(true)
                    c == '+' -> {
                        read()
                        if (peek() == ' ') read()
                        r[r.size - 1] =
                            r.last() + if (peek()!!.isDigit()) literal() else expression(true)
                    }
                    c == '*' -> {
                        read()
                        if (peek() == ' ') read()
                        if (proper) {
                            r += if (peek()!!.isDigit()) literal() else expression(true)
                        } else {
                            r[r.size - 1] = r.last() * if (peek()!!.isDigit()) literal() else expression(true)
                        }
                    }
                    c == ')' -> {
                        if (parentheses) read()
                        return r.reduce(Long::times)
                    }
                }
                if (peek() == ' ') read()
            }
            return r.reduce(Long::times)
        }

        fun literal() = buildString {
            while (peek()?.isDigit() == true) append(read())
        }.toLong()

        fun peek() = line.getOrNull(pos)
        fun read(): Char {
            val result = peek()
            pos++
            return result!!
        }
    }

}