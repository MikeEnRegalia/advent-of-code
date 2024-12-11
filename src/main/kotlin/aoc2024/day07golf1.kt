package aoc2024

fun main() = generateSequence(::readLine).map { it.split(" ", ": ").map(String::toLong) }.run {
    println(filter {
        fun List<Long>.check(x: Long): Boolean = if (isEmpty()) x == it[0] else
            sequenceOf(x + first(), x * first()).any { drop(1).check(it) }
        it.drop(1).check(0)
    }.sumOf { it.first() })
}

