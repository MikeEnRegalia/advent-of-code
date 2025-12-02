package aoc2025

fun main() {
    val ranges = readln().split(",")
        .map { s -> s.split("-").map(String::toLong).let { it[0]..it[1] } }

    var part1 = 0L
    var part2 = 0L

    for (range in ranges) for (id in range) {
        val s = id.toString()
        var b1 = false
        var b2 = false
        for (l in 1..s.length / 2) {
            val chunks = s.chunked(l)
            if (chunks.distinct().size == 1) {
                if (chunks.size == 2) {
                    b1 = true
                }
                b2 = true
            }
        }
        if (b1) part1 += id
        if (b2) part2 += id
    }

    println(part1)
    println(part2)
}
