package aoc2024

fun main() = generateSequence(::readLine).map { it.split("   ").map(String::toInt).zipWithNext()[0] }.unzip().run {
    println(first.sumOf { n -> n * second.count { it == n } })
}
