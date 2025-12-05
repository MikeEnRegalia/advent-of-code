package aoc2025

import kotlin.math.max
import kotlin.math.min

fun main() {
    val lines = generateSequence(::readLine).toList()
    val ranges = lines.filter { "-" in it }
        .map { it.split("-").map(String::toLong).let { it[0]..it[1] } }
    val ingredients = lines.filter { it.isNotEmpty() && "-" !in it }.map(String::toLong)

    println(ingredients.count { i -> ranges.any { i in it } })

    fun LongRange.overlapsWith(other: LongRange) = first in other || last in other
    fun LongRange.isIncludedIn(other: LongRange) = first in other && last in other

    with(ranges.toMutableSet()) {
        var sum = 0L
        while (isNotEmpty()) {
            val redundant = firstOrNull { r -> any { it != r && r.isIncludedIn(it) } }
            if (redundant != null){
                this -= redundant
                continue
            }

            val done = filter { r -> none { it != r && r.overlapsWith(it) } }
            this -= done.toSet()
            sum += done.sumOf { it.last + 1 - it.first }

            val r = filter { r -> any { it != r && r.overlapsWith(it) } }.randomOrNull() ?: continue
            val r2 = filter { it != r && r.overlapsWith(it) }.random()

            this -= r
            this -= r2
            this += min(r.first, r2.first)..max(r.last, r2.last)
        }
        println(sum)
    }
}
