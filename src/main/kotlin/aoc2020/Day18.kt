package aoc2020

fun main() {
    generateSequence(::readLine).map { AoC2020Day18.Parser(it).expression() }.sum().also { println(it) }
}

object AoC2020Day18 {
    class Parser(val line: String, var pos: Int = 0) {

        fun expression(parentheses: Boolean = false): Long {
            if (parentheses) read()
            var result = 0L
            while (pos != line.length) {
                val c = peek() ?: break
                when {
                    c.isDigit() -> result = literal()
                    c == '(' -> result = expression(true)
                    c == '+' -> {
                        read()
                        if (peek() == ' ') read()
                        result += if (peek()!!.isDigit()) literal() else expression(true)
                    }
                    c == '*' -> {
                        read()
                        if (peek() == ' ') read()
                        result *= if (peek()!!.isDigit()) literal() else expression(true)
                    }
                    c == ')' -> {
                        read()
                        return result
                    }
                }
                if (peek() == ' ') read()
            }
            return result
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