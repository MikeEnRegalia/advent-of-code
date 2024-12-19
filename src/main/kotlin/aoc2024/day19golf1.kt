package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    val cache = mutableMapOf("" to false)

    fun String.match(): Boolean = cache.getOrPut(this) {
        lines[0].split(", ").any {
            when {
                !startsWith(it) -> false
                equals(it) -> true
                else -> drop(it.length).match()
            }
        }
    }

    println(lines.drop(2).count(String::match))
}
