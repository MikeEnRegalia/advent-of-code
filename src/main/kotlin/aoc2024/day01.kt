package aoc2024

import kotlin.math.abs

fun main() {
    val data = generateSequence(::readLine).map { it.split("""\s+""".toRegex()).map(String::toInt) }.toList()
    val firsts = data.map { it[0] }.sorted()
    val seconds = data.map { it[1] }.sorted()

    println(firsts.withIndex().sumOf { (i, n) -> abs(seconds[i] - n) })
    println(firsts.sumOf { n -> n * seconds.filter { it == n }.size })
}
