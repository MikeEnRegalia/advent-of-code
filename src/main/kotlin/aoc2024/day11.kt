package aoc2024

fun main() {
    val originalStones = readln().split(" ").map(String::toLong).groupingBy { it }.eachCount()

    fun blink(times: Int): Long {
        var stones = originalStones.mapValues { it.value.toLong() }
        repeat(times) { i ->
            stones = buildMap {
                for ((n, a) in stones.entries) when {
                    n == 0L -> listOf(1L)
                    "$n".length % 2 == 0 -> "$n".let { it.chunked(it.length / 2).map(String::toLong) }
                    else -> listOf(n * 2024)
                }.forEach { compute(it) { _, oldA -> (oldA ?: 0) + a } }
            }
        }
        return stones.values.sum()
    }

    listOf(25, 75).forEach { println(blink(it)) }
}
