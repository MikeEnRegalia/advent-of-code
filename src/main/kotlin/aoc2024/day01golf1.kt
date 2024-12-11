package aoc2024

fun main() =
    generateSequence(::readLine).map { it.split("   ").map(String::toInt).zipWithNext()[0] }.unzip().run {
        println(first.sorted().zip(second.sorted()).sumOf { Math.abs(it.first - it.second) })
    }

