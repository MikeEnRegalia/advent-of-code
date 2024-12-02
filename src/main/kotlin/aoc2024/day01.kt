package aoc2024

import kotlin.math.abs

fun main() {
    val lines = String(System.`in`.readAllBytes()).trim().lines()
    val set1 = lines
        .map { it.split(" ").filterNot(String::isBlank)}
        .map { it.first() }
        .map(String::toInt)
        .sorted()

    val set2 = lines.map { it.split(" ").filterNot { it.isBlank()}[1]}.map { it.toInt() }.sorted()
    var sum = 0
    for ((i, n) in set1.withIndex()) {
        var m = set2[i]
        sum += abs(m - n)
    }

    println(sum)

    var sum2 = 0
    for (n in set1) {
        sum2 += n * set2.filter { it == n}.size
    }
    println(sum2)
}
