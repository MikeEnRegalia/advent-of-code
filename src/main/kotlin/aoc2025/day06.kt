package aoc2025

fun main() {
    val lines = generateSequence(::readLine).toList()

    fun String.operation(): (Long, Long) -> Long = if (this == "+") Long::plus else Long::times

    lines.map { it.split(" ").map(String::trim).filter(String::isNotBlank) }.let { data ->
        data.first().indices.sumOf { i ->
            data.dropLast(1).map { it[i].toLong() }.reduce(data.last()[i].operation())
        }
    }.also(::println)

    val columns = lines.first().indices

    var sum = 0L
    var from = 0

    while (from in columns) {
        val till = columns.drop(from).firstOrNull { i -> lines.all { it[i] == ' ' } } ?: (columns.last + 1)
        val numbers = (from..<till)
            .map { i -> lines.dropLast(1).map { it[i] }.joinToString("").trim().toLong() }
        sum += numbers.reduce(lines.last().drop(from).take(till - from).trim().operation())
        from = till + 1
    }

    println(sum)
}
