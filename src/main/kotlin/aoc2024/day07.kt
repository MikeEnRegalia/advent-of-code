package aoc2024

fun main() {
    val lines = generateSequence(::readLine).map {
        it.split(" ", ":").filter(String::isNotBlank).map(String::toLong)
    }.toList()

    fun List<List<Long>>.check(concat: Boolean) = mapNotNull { numbers ->
        fun List<Long>.test(n: Long, x: Long = 0L): Boolean {
            if (isEmpty()) return x == n
            val (first, rest) = first() to drop(1)
            return with(rest) {
                test(n, x + first) || test(n, x * first) || concat && test(n, "$x$first".toLong())
            }
        }
        numbers.first().takeIf { numbers.drop(1).test(it) }
    }.sum()

    println(lines.check(false))
    println(lines.check(true))
}
