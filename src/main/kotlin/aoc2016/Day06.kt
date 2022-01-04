package aoc2016

fun main() = generateSequence(::readLine).toList().let { lines ->
    (0 until lines.first().length)
        .map { i -> lines.mostFrequent { it[i] } }
        .let { cols -> listOf(cols.map { it.first().key }, cols.map { it.last().key }).map { it.joinToString("") } }
        .forEach(::println)
}

fun <T, K> List<T>.mostFrequent(s: (T) -> K) = groupingBy(s).eachCount().entries.sortedByDescending { it.value }
