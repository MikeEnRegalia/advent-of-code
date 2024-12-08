package aoc2024

fun main() = generateSequence(::readLine)
    .map { it.split(" ", ":").filter(String::isNotBlank).map(String::toLong) }.toList()
    .run {
        println(check(false))
        println(check(true))
    }

fun List<List<Long>>.check(concat: Boolean) = mapNotNull { numbers ->
    numbers.first().takeIf { numbers.test(concat) }
}.sum()

fun List<Long>.test(concat: Boolean, x: Long = 0L): Boolean = when (size) {
    1 -> x == first()
    else -> {
        val (first, rest) = get(1) to listOf(first()) + drop(2)
        with(rest) {
            test(concat, x + first) || test(concat, x * first) || concat && test(true, "$x$first".toLong())
        }
    }
}
