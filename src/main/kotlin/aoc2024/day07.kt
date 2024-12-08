package aoc2024

fun main() = generateSequence(::readLine)
    .map { it.split(" ", ":").filter(String::isNotBlank).map(String::toLong) }.toList()
    .run {
        println(check { a, b -> sequenceOf(a + b, a * b)})
        println(check { a, b -> sequenceOf(a + b, a * b, "$a$b".toLong())})
    }

fun List<List<Long>>.check(op: (a: Long, b: Long) -> Sequence<Long>) =
    filter { it.test(op) }.sumOf { it.first() }

fun List<Long>.test(op: (a: Long, b: Long) -> Sequence<Long>, x: Long = 0L): Boolean = when (size) {
    1 -> x == first()
    else -> {
        val (first, rest) = get(1) to listOf(first()) + drop(2)
        with(rest) {
            op(x, first).any { test(op, it) }
        }
    }
}
