#!/usr/bin/env kotlin

generateSequence { readlnOrNull() }.forEach { println("$it -> ${Calculator(it.replace(" ", "")).expression()}") }

class Calculator(val input: String) {
    private var pos: Int = 0
    fun parentheses(): Long {
        pos++
        val r = expression()
        pos++
        return r
    }

    fun expression(): Long {
        var r: Long? = null
        var lastOp: Char? = null
        while (pos in input.indices) when (val c = input[pos]) {
            '+', '*' -> lastOp = c
            ')' -> return r
            else -> {
                val v = if (c == '(') parentheses() else c.toString().toInt().toLong()
                if (r == null) r = v
                else if (lastOp == '+') r += v
                else r *= v
            }
        }
        return r ?: 0L
    }


    return expression()
}
