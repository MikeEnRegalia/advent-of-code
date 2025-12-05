package aoc2025

import kotlin.math.max
import kotlin.math.min

fun main() {
    val lines = generateSequence(::readLine).toList()
    val ranges = lines.filter { "-" in it }.map { it.split("-").map(String::toLong).let { it[0]..it[1] } }
    val ingredients = lines.filter { it.isNotEmpty() && "-" !in it }.map(String::toLong)

    println(ingredients.count { i -> ranges.any { i in it } })

    fun LongRange.overlapsWith(other: LongRange) = first in other || last in other
    fun LongRange.isIncludedIn(other: LongRange) = first in other && last in other

    with(ranges.toMutableSet()) {
        while (any { r -> none { it != r && r.overlapsWith(it) } }) {
            val redundant = filter { r -> any { it != r && r.isIncludedIn(it) } }
            if (redundant.isNotEmpty()) {
                this -= redundant
                continue
            }

            val r = firstOrNull { r -> any { it != r && r.overlapsWith(it) } } ?: break
            val r2 = first { it != r && r.overlapsWith(it) }
            this += min(r.first, r2.first)..max(r.last, r2.last)
        }
        println(sumOf { it.last + 1 - it.first })
    }
}

