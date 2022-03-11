package aoc2016

fun main() {
    val blocked = generateSequence(::readLine)
        .map { row -> row.split("-").map(String::toLong).let { it[0]..it[1] } }
        .toList()

    var ip = 0L
    var sum = 0L
    while (ip <= 4294967295) {
        val blockedBy = blocked.filter { ip in it }
        if (blockedBy.isNotEmpty()) {
            ip = blockedBy.maxOf { it.last } + 1
            continue
        }
        if (sum == 0L) println(ip)
        sum++
        ip++
    }
    println(sum)
}