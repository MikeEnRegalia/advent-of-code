package aoc2021b

fun main() {
    val l = generateSequence(::readLine).map { it.toInt() }.toList()
    var part1 = 0
    var part2 = 0
    for (i in l.indices) {
        if (i > 0 && l[i] > l[i-1]) part1++
        if (i > 2 && l[i] > l[i-3]) part2++
    }
    println(part1)
    println(part2)
}