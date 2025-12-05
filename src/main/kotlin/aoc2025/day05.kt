package aoc2025

fun main() {
    val lines = generateSequence(::readLine).toList()
    val ranges = lines.filter { "-" in it}.map { it.split("-").map(String::toLong).let { it[0]..it[1] } }
    val ingredients = lines.filter { it.isNotEmpty() && "-" !in it}.map(String::toLong)
    println(ingredients.count { i -> ranges.any { i in it }})
}
