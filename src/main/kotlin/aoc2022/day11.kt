package aoc2022

fun main() = day11(String(System.`in`.readAllBytes())).forEach(::println)

private fun day11(input: String): List<Any?> {
    data class Monkey(
        var items: MutableList<Long>,
        val operation: (Long) -> Long,
        val divBy: Long,
        val ifTrue: Int,
        val ifFalse: Int
    ) {
        fun test(item: Long) = if (item % divBy == 0L) ifTrue else ifFalse
    }

    val monkeys = input.split("\n\n").map { it.split("\n") }.map { lines ->
        val items = lines[1].split(" ", ",").mapNotNull(String::toLongOrNull).toMutableList()
        val opChar = lines[2][lines[2].lastIndexOf(" ") - 1]
        val operand = lines[2].substringAfterLast(" ")
        val operation: (Long) -> Long = when (opChar) {
            '+' -> { old -> old + (operand.toLongOrNull() ?: old) }
            '*' -> { old -> old * (operand.toLongOrNull() ?: old) }
            else -> throw IllegalArgumentException()
        }
        val divBy = lines[3].split(" ").mapNotNull(String::toLongOrNull).single()
        val monkeyIfTrue = lines[4].split(" ").mapNotNull(String::toIntOrNull).single()
        val monkeyIfFalse = lines[5].split(" ").mapNotNull(String::toIntOrNull).single()
        Monkey(items, operation, divBy, monkeyIfTrue, monkeyIfFalse)
    }

    val inspections = mutableMapOf<Int, Long>()
    repeat(20) {
        for ((monkeyIndex, monkey) in monkeys.withIndex()) {
            inspections[monkeyIndex] = inspections.getOrDefault(monkeyIndex, 0) + monkey.items.size
            monkey.items.map { monkey.operation(it) / 3 }
                .forEach {
                    monkeys[monkey.test(it)].items += it
                }
            monkey.items.clear()
        }
    }
    return listOf(inspections.values.sortedDescending().take(2).reduce(Long::times), null)
}

