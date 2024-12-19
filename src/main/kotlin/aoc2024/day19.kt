package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    val towels = lines.first().split(", ")
    val patterns = lines.drop(2)

    val cache = mutableMapOf<String, Boolean>()
    val part1 = patterns.count { pattern ->
        fun foo(p: String): Boolean = when {
            p.isBlank() -> true
            cache[p] != null -> cache[p]!!
            else -> towels.filter { p.startsWith(it) }.any { foo(p.drop(it.length)) }
        }.also { cache[p] = it }
        foo(pattern)
    }

    println(part1)
}
