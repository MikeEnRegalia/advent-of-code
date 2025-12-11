package aoc2025

fun main() {
    val links = generateSequence(::readLine)
        .associate { line -> line.split(": ").let { it[0] to it[1].split(" ") } }

    val cache = mutableMapOf<Pair<String, Set<String>>, Long>()

    fun follow(curr: String, target: String, visit: Set<String> = setOf(), seen: Set<String> = setOf()): Long =
        cache.getOrPut(curr to seen) {
            when {
                curr == target -> if (seen == visit) 1 else 0
                else -> links.getValue(curr).sumOf {
                    follow(it, target, visit, if (it in visit) seen + it else seen)
                }
            }
        }

    println(follow("you", "out"))
    println(follow("svr", "out", setOf("fft", "dac")))
}
