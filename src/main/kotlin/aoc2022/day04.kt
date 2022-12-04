package aoc2022

fun main() = day04(String(System.`in`.readAllBytes())).forEach(::println)

fun day04(input: String) = input.lines()
    .map { it.split(",", "-").map(String::toInt) }
    .map { (a, b, c, d) -> a..b to c..d }
    .run {
        listOf(
            count { (a, b) -> a.includes(b) || b.includes(a) },
            count { (a, b) -> a.overlapsWith(b) || b.overlapsWith(a) },
        )
    }

fun IntRange.overlapsWith(o: IntRange) = any { it in o }
fun IntRange.includes(o: IntRange) = o.all { it in this }
