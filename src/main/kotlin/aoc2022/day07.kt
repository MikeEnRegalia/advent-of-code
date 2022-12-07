package aoc2022

fun main() = day07(String(System.`in`.readAllBytes())).forEach(::println)

// this is the refactored solution, it is "inspired" by many other solutions and authors.
// if you are interested in my original solution you are free to look at the history.
private fun day07(input: String): List<Any?> {
    fun List<String>.cd(dir: String) = when (dir) {
        ".." -> dropLast(1)
        "/" -> emptyList()
        else -> plus(dir)
    }

    val sizes = mutableMapOf<List<String>, Int>()
    fun List<String>.add(size: Int) = indices.map { subList(0, it + 1) }
        .forEach { sizes[it] = (sizes[it] ?: 0) + size }

    var dir = listOf<String>()
    for (line in input.lines()) when {
        line.startsWith("$ cd ") -> dir = dir.cd(line.substringAfterLast(" "))
        else -> line.split(" ").first().toIntOrNull()?.let(dir::add)
    }

    val toFree = sizes.filterKeys { it.size == 1 }.values.sum() - 40_000_000

    return with(sizes.values) { listOf(filter { it <= 100_000 }.sum(), filter { it >= toFree }.min()) }
}
