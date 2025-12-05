package aoc2025

import kotlin.math.max
import kotlin.math.min

fun main() {
    val (rangeData, ingredientsData) = generateSequence(::readLine).partition { "-" in it }
    val ranges = rangeData.map { line -> line.split("-").map(String::toLong).let { it[0]..it[1] } }

    ingredientsData.mapNotNull(String::toLongOrNull).count { i -> ranges.any { i in it } }
        .also(::println)

    fun LongRange.overlapsWith(other: LongRange) = first in other || last in other
    fun LongRange.isIncludedIn(other: LongRange) = first in other && last in other

    with(ranges.toMutableSet()) {
        while (any { r -> none { it != r && r.overlapsWith(it) } }) {
            filter { r -> any { it != r && r.isIncludedIn(it) } }.forEach(::remove)

            val r = firstOrNull { r -> any { it != r && r.overlapsWith(it) } } ?: break
            val r2 = first { it != r && r.overlapsWith(it) }
            this += min(r.first, r2.first)..max(r.last, r2.last)
        }
        println(sumOf { it.last + 1 - it.first })
    }
}

