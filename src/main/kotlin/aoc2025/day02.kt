package aoc2025

fun main() {
    val ranges = readln().split(",").map { it.split("-").map(String::toLong) }

    var part1 = 0L
    var part2 = 0L

    for (range in ranges) for (id in range[0]..range[1]) {
        val s = id.toString()
        for (l in s.length / 2 downTo 1) {
            val chunks = s.chunked(l)
            if (chunks.distinct().size == 1) {
                if (chunks.size == 2) part1 += id
                part2 += id
                break
            }
        }
    }

    println(part1)
    println(part2)
}
