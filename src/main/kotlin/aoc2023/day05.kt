package aoc2023

fun main() = day05(String(System.`in`.readAllBytes())).forEach(::println)

private fun day05(input: String): List<Any?> {
    val chunks = input.split("\n\n")
    val seeds = chunks[0].split(" ").mapNotNull(String::toLongOrNull)
    val conversions = chunks.drop(1).map { chunk ->
        chunk.split("\n").drop(1)
            .map { it.split(" ").map(String::toLong) }
    }

    var data = seeds.toSet()
    for (conversion in conversions) {
        val newData = data.map { d ->
            conversion.firstNotNullOfOrNull {
                val dest = it[0]
                val src = it[1]
                val l = it[2]
                if (d in src until src + l) dest + (d - src) else null
            } ?: d
        }
        data = newData.toSet()
    }

    return listOf(data.min())
}