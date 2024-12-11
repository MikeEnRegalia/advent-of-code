package aoc2024

import util.mapToCountAsLong

fun main() {
    var stones = readln().split(" ").map(String::toLong).mapToCountAsLong()
    repeat(75) { i ->
        stones = stones.flatMap { (stone, n) ->
            when {
                stone == 0L -> listOf(1L to n)
                "$stone".length % 2 == 0 -> "$stone".let { it.chunked(it.length / 2).map { s -> s.toLong() to n } }
                else -> listOf(stone * 2024 to n)
            }
        }.associate { (stone, n) -> stone to (stones.getOrDefault(n, 0L) + n) }
        if (i + 1 in listOf(25, 75)) println(stones.values.sum())
    }
}
