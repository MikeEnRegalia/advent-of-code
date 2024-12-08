package aoc2024

typealias Operations = (a: Long, b: Long) -> Sequence<Long>

fun main() = generateSequence(::readLine)
    .map { it.split(" ", ":").filter(String::isNotBlank).map(String::toLong) }.toList()
    .run {
        println(test { a, b -> sequenceOf(a + b, a * b) })
        println(test { a, b -> sequenceOf(a + b, a * b, "$a$b".toLong()) })
    }

fun List<List<Long>>.test(op: Operations) = filter { it.test(op) }.sumOf(List<Long>::first)

fun List<Long>.test(op: Operations, x: Long = 0L): Boolean = when (size) {
    1 -> x == first()
    else -> op(x, get(1)).any { (listOf(first()) + drop(2)).test(op, it) }
}
