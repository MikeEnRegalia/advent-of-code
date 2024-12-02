package aoc2024

fun main() {
    fun List<Int>.check(): Boolean {
        for ((i, n) in withIndex()) {
            if (i == 0) continue
            val diff = n - get(i - 1)
            if (diff !in 1..3) {
                return false
            }
        }

        return true
    }

    val lines = String(System.`in`.readAllBytes()).trim().lines()

    val part1 = lines.count {
        val x = it.split(" ").map(String::toInt)
        val z = if (x.first() < x.last()) x else x.reversed()
        z.check()
    }
    println(part1)

    val part2 = lines.count {
        val x = it.split(" ").map(String::toInt)
        val z = if (x.first() < x.last()) x else x.reversed()
        if (z.check()) return@count true

        for (j in z.indices) {
            val z2 = z.take(j) + z.drop(j + 1)
            if (z2.check()) return@count true
        }
        false
    }
    println(part2)
}
