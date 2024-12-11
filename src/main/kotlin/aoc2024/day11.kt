package aoc2024

fun main() {
    var stones = readln().split(" ").map(String::toLong).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    repeat(75) { i ->
        stones = buildMap {
            for ((stone, n) in stones) when {
                stone == 0L -> listOf(1L)
                "$stone".length % 2 == 0 -> "$stone".let { it.chunked(it.length / 2).map(String::toLong) }
                else -> listOf(stone * 2024)
            }.forEach { compute(it) { _, oldN -> (oldN ?: 0) + n } }
        }
        if ( i+1 in listOf(25, 75)) println(stones.values.sum())
    }
}
