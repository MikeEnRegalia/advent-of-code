package aoc2022

fun main() = day07(String(System.`in`.readAllBytes())).forEach(::println)

// this is the refactored solution, it is "inspired" by many other solutions and authors.
// if you are interested in my original solution you are free to look at the history.
private fun day07(input: String): List<Any?> {
    var dir = listOf<String>()
    val sizes = mutableMapOf<List<String>, Int>()
    var used = 0

    fun List<String>.cd(dir: String) = when (dir) {
        ".." -> dropLast(1)
        "/" -> emptyList()
        else -> plus(dir)
    }

    fun List<String>.add(size: Int) = indices.map { subList(0, it + 1) }.forEach { path ->
        sizes[path] = (sizes[path] ?: 0) + size
    }

    for (line in input.lines()) when {
        line.startsWith("$ cd ") -> dir = dir.cd(line.substringAfterLast(" "))
        else -> line.split(" ").first().toIntOrNull()?.also { size ->
            dir.add(size)
            used += size
        }
    }

    return with(sizes.values) {
        listOf(filter { it <= 100_000 }.sum(), filter { it >= used - 40_000_000 }.min())
    }
}
