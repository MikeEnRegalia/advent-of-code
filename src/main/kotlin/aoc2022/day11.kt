package aoc2022

fun main() {
    data class Monkey(val items: MutableList<Long>, val operation: (Long) -> Long, val test: Triple<Int, Int, Int>) {
        var inspections: Long = 0
        fun test(item: Long) = (if (item % test.first == 0L) test.second else test.third).also { inspections++ }
    }

    val input = System.`in`.reader().readText().split("\n\n").map { it.split("\n") }
    fun doMonkeyBusiness(rounds: Int, div: Boolean): Long {
        val monkeys = input.map { lines ->
            val items = lines[1].split(" ", ",").mapNotNull(String::toLongOrNull).toMutableList()
            val opChar = lines[2][lines[2].lastIndexOf(" ") - 1]
            val operand = lines[2].substringAfterLast(" ").toIntOrNull()
            val operation: (Long) -> Long = when {
                opChar == '+' -> ({ it + operand!! })
                operand != null -> ({ it * operand })
                else -> ({ it * it })
            }
            val divBy = lines[3].split(" ").mapNotNull(String::toIntOrNull).single()
            val ifTrue = lines[4].split(" ").mapNotNull(String::toIntOrNull).single()
            val ifFalse = lines[5].split(" ").mapNotNull(String::toIntOrNull).single()
            Monkey(items, operation, Triple(divBy, ifTrue, ifFalse))
        }
        val commonProduct by lazy { monkeys.map { it.test.first }.reduce(Int::times) }
        fun Long.relax() = if (div) this / 3 else this % commonProduct

        repeat(rounds) {
            for (monkey in monkeys) with(monkey) {
                while (items.isNotEmpty())
                    operation(items.removeFirst()).relax().also { monkeys[test(it)].items += it }
            }
        }
        return monkeys.map(Monkey::inspections).sortedDescending().take(2).reduce(Long::times)
    }
    listOf(doMonkeyBusiness(20, true), doMonkeyBusiness(10_000, false)).forEach(::println)
}

