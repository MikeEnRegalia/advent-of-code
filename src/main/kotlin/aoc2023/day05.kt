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

    var part1 = seeds.toSet()
    for (conversion in conversions) {
        val newData = part1.map { d ->
            conversion.firstNotNullOfOrNull {
                val dest = it[0]
                val src = it[1]
                val l = it[2]
                if (d in src..src + l) dest + (d - src) else null
            } ?: d
        }
        part1 = newData.toSet()
    }

    var data = seeds.chunked(2).map { it[0]..it[0] + it[1] }

    data class MapResult(val mapped: LongRange?, val remaining: Set<LongRange>)

    fun LongRange.convert(dest: Long, src: Long, l: Long): MapResult {
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

    return listOf(part1.min(), data.minOf { it.first })
}