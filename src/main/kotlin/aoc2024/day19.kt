package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    val towels = lines.first().split(", ")
    val patterns = lines.drop(2)

    val cache = mutableMapOf<String, List<List<String>>>()

    fun match(p: String): List<List<String>>? {
        return when {
            p.isBlank() -> listOf()
            towels.none { p.startsWith(it) } -> null
            p in cache -> cache.getValue(p)
            else -> towels
                .mapNotNull { if (p.startsWith(it)) it to p.drop(it.length) else null }
                .mapNotNull { (t, newP) ->
                    val match = match(newP) ?: return@mapNotNull null
                    if (match.isEmpty()) listOf(listOf(t)) else {
                        //println("matching $p with $t: $match")
                        match.map {
                            //println("match $p: $it")
                            listOf(t) + it
                        }
                    }
                }
                .flatten()
                .takeIf { it.isNotEmpty() }
                ?.also { cache[p] = it }
        }
    }

    println(patterns.count { p -> match(p).also { println("$p -> $it")}?.isNotEmpty() == true })

    println(cache.filterKeys { it in patterns }.values.sumOf { it.size})
}
