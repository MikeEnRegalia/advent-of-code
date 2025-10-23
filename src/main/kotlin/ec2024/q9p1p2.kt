package ec2024

fun main() {
    val brightnesses = generateSequence(::readLine).map { it.toInt() }.toList()

    fun Int.minimumBeetles(stamps: List<Int>, cache: MutableMap<Int, Int?> = mutableMapOf()): Int? = cache[this] ?: when {
        this == 0 -> 0
        else -> stamps
            .filter { this >= it }
            .mapNotNull { stamp -> (this - stamp).minimumBeetles(stamps, cache)?.let { it + 1 } }
            .minOfOrNull { it }
            .also { cache[this] = it }
    }

    val fewStamps = listOf(1, 3, 5, 10)
    println(brightnesses.sumOf { it.minimumBeetles(fewStamps)!! })

    val manyStamps = listOf(1, 3, 5, 10, 15, 16, 20, 24, 25, 30)
    println(brightnesses.sumOf { it.minimumBeetles(manyStamps)!! })
}
