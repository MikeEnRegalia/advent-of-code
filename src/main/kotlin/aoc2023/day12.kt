package aoc2023

fun main() {
    data class Record(val pattern: String, val numbers: List<Int>)

    val part1 = generateSequence(::readlnOrNull)
        .map { it.split(" ").let { (pattern, numbers) -> Record(pattern, numbers.split(",").map(String::toInt)) } }
        .toList()

    val part2 = part1.map { (pattern, numbers) ->
        Record(Array(5) { pattern }.joinToString("?"), Array(5) { numbers }.reduce { a, b -> a + b })
    }

    fun String.completed():List<Int> =
        "$this.".indexOf("#.").takeIf { it > -1 }?.takeIf { it < indexOf("?") }?.let { substring(0, it + 1) }
            ?.split(".")
            ?.filter { it.isNotEmpty()}
            ?.map { it.length } ?: listOf()

    fun String.supports(numbers: List<Int>, all: Boolean) = split(".")
        .filter(String::isNotEmpty)
        .map { it.length }
        .let { it == if (all) numbers else numbers.take(it.size) }

    fun String.solve(numbers: List<Int>): Long {
        val indexOfQ = indexOf("?").takeIf { it > -1 } ?: return if (supports(numbers, all = true)) 1L else 0L
        val modified = listOf(".", "#").map { substring(0, indexOfQ) + it + substring(indexOfQ + 1) }
        return modified.sumOf {
            val completed = it.completed()
            //println("considering $it ($completed)")
            if (completed == numbers.take(completed.size)) it.solve(numbers) else 0
        }
    }

    fun List<Record>.solve() = sumOf { (patternWithPlaceholders, numbers) ->
        println("$patternWithPlaceholders: $numbers")
        patternWithPlaceholders.solve(numbers)
    }

    println(part1.solve())
    println(part2.solve())

}

