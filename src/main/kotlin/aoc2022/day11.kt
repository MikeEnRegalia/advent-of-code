package aoc2022

fun main() = day11(String(System.`in`.readAllBytes())).forEach(::println)

private fun day11(input: String): List<Any?> {

    data class Item(private val initial: Int, private val remainders: Map<Int, Int>) {
        fun isDivisibleByPart1(d: Int) = initial % d == 0
        fun isDivisibleByPart2(d: Int) = remainders.getValue(d) == 0
        operator fun plus(x: Int) = r { it + x }
        operator fun times(x: Int) = r { it * x }
        operator fun div(x: Int) = r { it / x }
        fun squared() = r { it * it }
        private fun r(op: (Int) -> Int) = Item(op(initial), remainders.toMutableMap().apply {
            keys.forEach { prime -> set(prime, op(getValue(prime)) % prime) }
        })
    }

    fun Int.toItem() = Item(this, mutableMapOf<Int, Int>().apply {
        for (prime in listOf(2, 3, 5, 7, 9, 11, 13, 17, 19, 23)) set(prime, this@toItem % prime)
    })

    data class Monkey(
        val items: MutableList<Item>,
        val op: (Item) -> Item,
        val div: Int,
        val ifTrue: Int,
        val ifFalse: Int
    ) {
        fun test1(item: Item) = if (item.isDivisibleByPart1(div)) ifTrue else ifFalse
        fun test2(item: Item) = if (item.isDivisibleByPart2(div)) ifTrue else ifFalse
        override fun toString() = "$items"
    }

    fun loadMonkeys() = input.split("\n\n").map { it.split("\n") }.map { lines ->
        val items = lines[1].split(" ", ",").mapNotNull(String::toIntOrNull).toMutableList()
        val opChar = lines[2][lines[2].lastIndexOf(" ") - 1]
        val operand = lines[2].substringAfterLast(" ").toIntOrNull()
        val operation: (Item) -> Item = when (opChar) {
            '+' -> { old -> old + operand!! }
            '*' -> { old -> if (operand == null) old.squared() else old * operand }
            else -> throw IllegalArgumentException()
        }

        val divBy = lines[3].split(" ").mapNotNull(String::toIntOrNull).single()
        val monkeyIfTrue = lines[4].split(" ").mapNotNull(String::toIntOrNull).single()
        val monkeyIfFalse = lines[5].split(" ").mapNotNull(String::toIntOrNull).single()
        Monkey(items.map { it.toItem() }.toMutableList(), operation, divBy, monkeyIfTrue, monkeyIfFalse)
    }

    fun doMonkeyBusiness(rounds: Int, div: Boolean): Long {
        val monkeys = loadMonkeys()
        val inspections = mutableMapOf<Int, Long>()
        repeat(rounds) { round ->
            for ((i, monkey) in monkeys.withIndex()) with(monkey) {
                inspections[i] = inspections.getOrDefault(i, 0) + items.size
                items.map { op(it).let { if (div) it / 3 else it } }
                    .forEach { monkeys[if (div) test1(it) else test2(it)].items += it }
                items.clear()
            }
        }
        return inspections.values.sortedDescending().take(2).reduce(Long::times)
    }

    return listOf(doMonkeyBusiness(20, true), doMonkeyBusiness(10_000, false))
}