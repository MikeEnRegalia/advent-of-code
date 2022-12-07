package aoc2022

fun main() = day07(String(System.`in`.readAllBytes())).forEach(::println)

private fun day07(input: String): List<Any?> {
    val currentPath = mutableListOf<String>()
    val sizes = mutableMapOf<String, Long>()
    var used = 0L
    for (line in input.lines()) {
        if (line.startsWith("$ cd ")) with(currentPath) {
            when (val to = line.substringAfterLast(" ")) {
                ".." -> removeLast()
                "/" -> clear()
                else -> add(to)
            }
        }
        else {
            val size = line.split(" ").first().toIntOrNull() ?: continue
            var dir = "/"
            for (e in currentPath) {
                if (!dir.endsWith("/")) dir += "/"
                dir += e
                sizes.compute(dir) { _, prev -> (prev ?: 0L) + size }
            }
            used += size
        }
    }

    val total = 70000000L
    val needed = 30000000L
    val free = total - used
    val toFree = needed - free

    return with(sizes.values) {
        listOf(filter { it <= 100000 }.sum(), filter { it >= toFree }.min())
    }
}
