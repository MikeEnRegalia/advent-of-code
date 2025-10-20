package ec2024

import kotlin.math.abs

fun main() {
    val values = generateSequence(::readLine).map { it.toInt() }.toList()

    println((values.min()..values.max()).minOf { v -> values.sumOf { abs(it - v) } })
}
