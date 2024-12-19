package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    val towels = lines.first().split(", ")
    val patterns = lines.drop(2)
    val cache = mutableMapOf<String, Long>()

    fun String.match(): Long = if (this in cache) cache.getValue(this) else towels.sumOf { towel ->
        when {
            !startsWith(towel) -> 0L
            length == towel.length -> 1L
            else -> drop(towel.length).match()
        }
    }.also { cache[this] = it }

    with (patterns.map(String::match)) {
        println(count { it > 0 })
        println(sum())
    }
}
