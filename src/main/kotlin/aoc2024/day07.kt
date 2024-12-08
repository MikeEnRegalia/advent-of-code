package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    fun List<String>.check(concat: Boolean) = mapNotNull { line ->
        val numbers = line.split(" ", ":").filter(String::isNotBlank).map(String::toLong)
        fun List<Long>.test(x: Long, n: Long): Boolean {
            if (isEmpty()) return x == n
            val (first, rest) = first() to drop(1)
            val (a, b, c) = Triple(x * first, x + first, "$x$first".toLong())
            if (size == 1 && (a == n || b == n || concat && c == n)) return true
            return rest.test(b, n) || rest.test(a, n) || concat && rest.test(c, n)
        }

        numbers.first().takeIf { numbers.drop(1).test(0L, it) }
    }.sum()

    println(lines.check(false))
    println(lines.check(true))
}
