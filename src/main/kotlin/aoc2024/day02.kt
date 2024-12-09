package aoc2024

fun main() {
    fun prepare(record: String) = record.split(" ").map(String::toInt)
        .let { if (it.first() < it.last()) it else it.reversed() }
    fun check(l: List<Int>) = l.windowed(2).map { it[1] - it[0] }.all { it in 1..3 }

    with(generateSequence(::readLine).map(::prepare).toList()) {
        println(count(::check))
        println(count { check(it) || it.indices.map { j -> it.take(j) + it.drop(j + 1) }.any(::check) })
    }
}
