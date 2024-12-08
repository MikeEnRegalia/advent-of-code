package aoc2024

typealias Operations = (a: Long, b: Long) -> Sequence<Long>

fun main() = generateSequence(::readLine)
    .map { it.split(" ", ":").filter(String::isNotBlank).map(String::toLong) }.toList()
    .let { numberLists ->
        sequenceOf<Operations>(
            { a, b -> sequenceOf(a + b, a * b) },
            { a, b -> sequenceOf(a + b, a * b, "$a$b".toLong()) })
            .map { op -> numberLists.filter { it.check(op) }.sumOf(List<Long>::first) }
            .forEach(::println)
    }

fun List<Long>.check(op: Operations, x: Long = 0L): Boolean = when (size) {
    1 -> x == first()
    else -> op(x, get(1)).any { (listOf(first()) + drop(2)).check(op, it) }
}
