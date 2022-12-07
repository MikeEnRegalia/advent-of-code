package aoc2022

fun main() = day07(String(System.`in`.readAllBytes())).forEach(::println)

private fun day07(input: String): List<Any?> {
    val currentPath = mutableListOf<String>()
    fun MutableList<String>.cd(dir: String): Any = when (dir) {
        ".." -> removeLast()
        "/" -> clear()
        else -> add(dir)
    }

    val sizes = mutableMapOf<List<String>, Int>()
    var used = 0

    for (line in input.lines()) {
        if (line.startsWith("$ cd ")) currentPath.cd(line.substringAfterLast(" "))
        else {
            val size = line.split(" ").first().toIntOrNull() ?: continue
            currentPath.indices.map { currentPath.subList(0, it + 1).toList() }.forEach { path ->
                sizes[path] = (sizes[path] ?: 0) + size
            }
            used += size
        }
    }

    return with(sizes.values) {
        listOf(filter { it <= 100_000 }.sum(), filter { it >= used - 40_000_000 }.min())
    }
}
