package aoc2022

fun main() = day07(String(System.`in`.readAllBytes())).forEach(::println)

private fun day07(input: String): List<Any?> {
    val currentPath = mutableListOf<String>()
    val sizes = mutableMapOf<String, Int>()
    var used = 0
    for (line in input.lines()) {
        if (line.startsWith("$ cd ")) {
            with(currentPath) {
                when (val to = line.substringAfterLast(" ")) {
                    ".." -> removeLast()
                    "/" -> clear()
                    else -> add(to)
                }
            }
            continue
        }
        val size = line.split(" ").first().toIntOrNull() ?: continue
        var dir = "/"
        for (path in currentPath) {
            if (!dir.endsWith("/")) dir += "/"
            dir += path
            sizes.compute(dir) { _, prev -> (prev ?: 0) + size }
        }
        used += size
    }

    return with(sizes.values) {
        listOf(filter { it <= 100_000 }.sum(), filter { it >= used - 40_000_000 }.min())
    }
}
