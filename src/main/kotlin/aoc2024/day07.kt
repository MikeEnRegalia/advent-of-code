package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    fun List<String>.combine(concat: Boolean) = mapNotNull { line ->
        val numbers = line.split(" ", ":").filter { it.isNotBlank()} .map(String::toLong)
        fun List<Long>.combine(base: Long, target: Long): Boolean {
            if (isEmpty()) {
                return base == target
            }
            val first = first()
            if (size == 1 && (base * first == target || base + first == target || concat && "$base$first".toLong() == target)) {
                return true
            }
            return drop(1).combine(base + first, target)
                    || drop(1).combine(base * first, target)
                    || concat && drop(1).combine("$base$first".toLong(), target)
        }

        val target = numbers.first()
        val args = numbers.drop(1)
        target.takeIf { args.combine(0L, it) }
    }.sum()

    println(lines.combine(false))
    println(lines.combine(true))
}
