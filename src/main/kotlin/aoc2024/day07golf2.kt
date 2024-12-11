package aoc2024

fun main() = generateSequence(::readLine).map { it.split(" ", ": ").map(String::toLong) }.run {
        println(sumOf {
            fun List<Long>.check(x: Long): Boolean = if (isEmpty()) x == it[0] else first().let {
                sequenceOf(x + it, x * it, "$x$it".toLong()).any { drop(1).check(it) }
            }
            if (it.drop(1).check(0)) it[0] else 0
        })
    }

