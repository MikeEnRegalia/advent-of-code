package aoc2024

fun main() {
    val originalNumbers = readln().split(" ").map { it.toLong() }.groupingBy { it }.eachCount()

    fun blink(times: Int) = originalNumbers.mapValues { it.value.toLong() }.toMutableMap().apply {
        repeat(times) { i ->
            val entries = toMap().entries.toList()
            clear()
            for ((n, a) in entries) {
                val newNumbers = when {
                    n == 0L -> listOf(1L)
                    "$n".length % 2 == 0 -> "$n".let { digits ->
                        listOf(digits.take(digits.length / 2).toLong(), digits.drop(digits.length / 2).toLong())
                    }

                    else -> listOf(n * 2024)
                }
                newNumbers.forEach { compute(it) { _, oldA -> (oldA ?: 0) + a } }
            }
        }
    }.values.sum()

    println(blink(25))
    println(blink(75))
}
