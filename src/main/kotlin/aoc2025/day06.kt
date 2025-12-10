package aoc2025

fun main() {
    val lines = generateSequence(::readLine).toList()

    lines.map { it.split(" ").map(String::trim).filter { it.isNotBlank() } }.let { data ->
        data.first().indices.sumOf { i ->
            data.dropLast(1).map { it[i].toLong() }.reduce(
                when (data.last()[i]) {
                    "+" -> Long::plus
                    else -> Long::times
                }
            )
        }
    }.also(::println)

}
