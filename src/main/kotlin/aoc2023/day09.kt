package aoc2023

fun main() = day09(generateSequence(::readlnOrNull).toList()).forEach(::println)

private fun day09(lines: List<String>): List<Any?> {
    fun String.predict(): Pair<Int, Int> {
        val data = mutableListOf<MutableList<Int>>()
        data += split(" ").map(String::toInt).toMutableList()
        while (!data.last().all { it == 0 }) {
            data += data.last().windowed(2).map { it[1] - it[0] }.toMutableList()
        }
        data.indices.reversed().forEach { i ->
            data[i] += if (i == data.indices.last) 0 else data[i][data[i].size - 1] + data[i + 1].last()
        }
        data.indices.reversed().forEach { i ->
            data[i].add(0, if (i == data.indices.last) 0 else data[i][0] - data[i + 1].first())
        }
        return data.first().first() to data.first().last()
    }

    val data = lines.map { it.predict() }
    return listOf(data.sumOf { it.second }, data.sumOf { it.first })
}