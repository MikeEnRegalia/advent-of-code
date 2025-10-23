package ec2024

fun main() {
    val brightnesses = generateSequence(::readLine).map { it.toInt() }.toList()

    val stamps = listOf(1, 3, 5, 10, 15, 16, 20, 24, 25, 30, 37, 38, 49, 50, 74, 75, 100, 101).sortedDescending()

    val cache = mutableMapOf<Int, Int?>()
    fun Int.minimumBeetles(): Int? = cache.getOrPut(this) {
        if (this == 0) 0 else stamps
            .filter { this >= it }
            .mapNotNull { stamp -> (this - stamp).minimumBeetles()?.let { it + 1 } }
            .minOfOrNull { it }
    }

    fun Int.minimumBeetlesTwoBalls() = (this / 2 - 50..this / 2)
        .filter { this - 2 * it <= 100 }
        .map { a -> listOf(a, this - a).mapNotNull { it.minimumBeetles() } }
        .minOf { it.sum() }

    println(brightnesses.sumOf { brightness -> brightness.minimumBeetlesTwoBalls() })
}
