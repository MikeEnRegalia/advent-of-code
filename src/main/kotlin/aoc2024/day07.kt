package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    fun List<String>.check(concat: Boolean) = mapNotNull { line ->
        val numbers = line.split(" ", ":").filter(String::isNotBlank).map(String::toLong)
        fun List<Long>.test(base: Long, target: Long): Boolean {
            if (isEmpty()) return base == target
            val first = first()
            if (size == 1) {
                if (base * first == target || base + first == target || concat && "$base$first".toLong() == target)
                    return true
            }
            val next = drop(1)
            return next.test(base + first, target)
                    || next.test(base * first, target)
                    || concat && next.test("$base$first".toLong(), target)
        }

        val target = numbers.first()
        val args = numbers.drop(1)
        target.takeIf { args.test(0L, it) }
    }.sum()

    println(lines.check(false))
    println(lines.check(true))
}
