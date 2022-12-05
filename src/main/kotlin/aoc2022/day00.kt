package aoc2022

import kotlin.math.min

fun main() = day00(String(System.`in`.readAllBytes())).forEach(::println)

fun day00(input: String): List<Any?> {
    fun String.parseLine() = this
    val lines = input.lines().map(String::parseLine)

    return listOf(null, null)
}

private fun <S> dijkstra(start: S, neighbors: (S) -> Iterable<S>): Map<S, Int> {
    var s = start
    val v = mutableSetOf<S>()
    val u = mutableSetOf<S>()
    val d = mutableMapOf(s to 0)
    while (true) {
        neighbors(s).filter { it !in v }.forEach { n ->
            u += n
            val distance = d.getValue(s) + 1
            d.compute(n) { _, old -> min(distance, old ?: Int.MAX_VALUE) }
        }
        v += s
        u -= s
        s = u.randomOrNull() ?: break
    }
    return d
}

// TODO Json parsing
// TODO MD5
