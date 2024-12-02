package aoc2024

fun main() {
    fun prepare(record: String) = record.split(" ").map(String::toInt).let {
        if (it.first() < it.last()) it else it.reversed()
    }

    fun check(l: List<Int>) = l.windowed(2).map { it[1] - it[0] }.all { it in 1..3 }

    val lines = String(System.`in`.readAllBytes()).trim().lines()

    val part1 = lines.map(::prepare).count(::check)
    println(part1)

    val part2 = lines.map(::prepare).count {
        if (check(it)) return@count true

        for (j in it.indices) {
            if (check(it.take<Int>(j) + it.drop<Int>(j + 1))) return@count true
        }
        false
    }
    println(part2)
}
