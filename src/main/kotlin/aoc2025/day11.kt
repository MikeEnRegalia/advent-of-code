package aoc2025

fun main() {
    val links = generateSequence(::readLine).associate { line -> line.split(": ").let { it[0] to it[1].split(" ") } }

    val cache = mutableMapOf<Pair<String, Set<String>>, Long>()

    fun String.findOut(visit: Set<String> = setOf(), seen: Set<String> = setOf()): Long = cache.getOrPut(this to seen) {
        when {
            this == "out" -> if (seen == visit) 1 else 0
            else -> links.getValue(this).sumOf { it.findOut(visit, if (it in visit) seen + it else seen) }
        }
    }

    println("you".findOut())
    println("svr".findOut(setOf("fft", "dac")))
}
