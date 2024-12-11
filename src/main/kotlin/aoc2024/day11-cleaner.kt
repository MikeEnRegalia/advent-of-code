package aoc2024

fun main() {
    val input = readln().split(" ").map(String::toLong).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    (1..75).fold(input) { acc, i ->
        acc.flatMap { (stone, n) ->
            when {
                stone == 0L -> listOf(1L to n)
                "$stone".length % 2 == 0 -> "$stone".let { it.chunked(it.length / 2).map { s -> s.toLong() to n } }
                else -> listOf(stone * 2024 to n)
            }
        }.associate { (stone, n) -> stone to (acc.getOrDefault(stone, 0L) + n) }
            .also { println(it.values.sum()) }
    }
}
