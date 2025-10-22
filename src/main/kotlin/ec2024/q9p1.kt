package ec2024

fun main() {
    val brightnesses = generateSequence(::readLine).map { it.toInt() }.toList()

    val cache = mutableMapOf<Int, Int?>()

    fun Int.minimumBeetles(stamps: List<Int>): Int? = when {
        this == 0 -> 0
        this in cache -> cache[this]
        else -> stamps.filter { this >= it }.mapNotNull { stamp ->
            val remainder = this - stamp
            remainder.minimumBeetles(stamps)?.let { it + 1 }
        }.minOfOrNull { it }
            .also { cache[this] = it }
    }

    val fewStamps = listOf(1, 3, 5, 10)
    println(brightnesses.sumOf { it.minimumBeetles(fewStamps)!! })

    cache.clear()

    val manyStamps = listOf(1, 3, 5, 10, 15, 16, 20, 24, 25, 30)
    println(brightnesses.sumOf { it.minimumBeetles(manyStamps).also(::println)!! })
}
