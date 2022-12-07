package aoc2022

fun main() = day07(String(System.`in`.readAllBytes())).forEach(::println)

private fun day07(input: String): List<Any?> {
    var dir = listOf<String>()
    val sizes = mutableMapOf<List<String>, Int>()
    var used = 0

    fun List<String>.cd(dir: String) = when (dir) {
        ".." -> subList(0, size - 1)
        "/" -> listOf()
        else -> plus(dir)
    }

    fun addFile(size: Int) {
        dir.indices.map { dir.subList(0, it + 1).toList() }.forEach { path ->
            sizes[path] = (sizes[path] ?: 0) + size
        }
        used += size
    }

    for (line in input.lines()) when {
        line.startsWith("$ cd ") -> dir = dir.cd(line.substringAfterLast(" "))
        else -> line.split(" ").first().toIntOrNull()?.let { addFile(it) }
    }

    return with(sizes.values) { listOf(filter { it <= 100_000 }.sum(), filter { it >= used - 40_000_000 }.min()) }
}
