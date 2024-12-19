package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    val cache = mutableMapOf<String, Boolean>()

    fun String.match(): Boolean = cache.getOrPut(this) {
        lines[0].split(", ").any { towel ->
            when {
                !startsWith(towel) -> false
                equals(towel) -> true
                else -> drop(towel.length).match()
            }
        }
    }

    println(lines.drop(2).count(String::match))
}
