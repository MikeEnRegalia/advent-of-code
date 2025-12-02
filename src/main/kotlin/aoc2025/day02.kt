package aoc2025

fun main() {
    val ranges = readln().split(",")
        .map { s -> s.split("-").map(String::toLong).let { it[0]..it[1] } }

    var part1 = 0L
    var part2 = 0L

    for (range in ranges) for (id in range) {
        val s = id.toString()
        for (l in (1..s.length / 2).reversed()) {
            val chunks = s.chunked(l)
            if (chunks.distinct().size == 1) {
                if (chunks.size == 2) {
                    part1 += id
                }
                part2 += id
                break
            }
        }
    }

    println(part1)
    println(part2)
}
