package aoc2023

import kotlin.math.max
import kotlin.math.min

fun main() = day05(String(System.`in`.readAllBytes())).forEach(::println)

private fun day05(input: String): List<Any?> {
    val chunks = input.split("\n\n")
    val seeds = chunks[0].split(" ").mapNotNull(String::toLongOrNull)
    val conversions = chunks.drop(1).map { chunk ->
        chunk.split("\n").drop(1)
            .map { it.split(" ").map(String::toLong) }
    }

    val part1 = seeds.map { it..it }
    val part2 = seeds.chunked(2).map { it[0]..it[0] + it[1] }

    return listOf(part1, part2).map { convert(conversions, it) }
}

private fun convert(conversions: List<List<List<Long>>>, seeds: List<LongRange>): Long {
    var data = seeds
    for (conversion in conversions) {
        val newData = mutableListOf<LongRange>()
        val oldData = data.toMutableList()
        val remainingData = mutableListOf<LongRange>()
        for ((dest, src, l) in conversion) {
            remainingData.clear()
            while (oldData.isNotEmpty()) {
                val oldRange = oldData.removeFirst()
                val (mapped, remaining) = oldRange.convert(dest, src, l)
                if (mapped != null) newData += mapped
                remainingData.addAll(remaining)
            }
            oldData.addAll(remainingData)
        }
        data = remainingData + newData
    }
    return data.minOf { it.first }
}

private data class MapResult(val mapped: LongRange?, val remaining: Set<LongRange>)

private fun LongRange.convert(dest: Long, src: Long, l: Long): MapResult {
    val delta = dest - src
    val srcRange = src until src + l
    val mapped = when {
        last < srcRange.first || srcRange.last < first -> null
        else -> max(srcRange.first, first)..min(srcRange.last, last)
    }
    if (mapped == null) return MapResult(null, setOf(this))

    val newData = mapped.first + delta..mapped.last + delta
    val oldData = buildSet {
        if (mapped.first > first) add(first until mapped.first)
        if (mapped.last < last) add(mapped.last + 1..last)
    }
    return MapResult(newData, oldData)
}

