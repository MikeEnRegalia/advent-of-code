package aoc2022

fun main() = day11(String(System.`in`.readAllBytes())).forEach(::println)

private fun day11(input: String): List<Any?> {

    data class Item(private val initial: Int, private val remainders: Map<Int, Int>) {
        fun rem(d: Int, useInitial: Boolean) = (if (useInitial) (initial % d) else remainders.getValue(d)) == 0
        operator fun plus(x: Int) = transform { it + x }
        operator fun times(x: Int) = transform { it * x }
        operator fun div(x: Int) = transform { it / x }
        fun squared() = transform { it * it }
        private fun transform(op: (Int) -> Int) = Item(op(initial), remainders.toMutableMap().apply {
            keys.forEach { prime -> set(prime, op(getValue(prime)) % prime) }
        })
    }

    fun Int.toItem() = Item(this, mutableMapOf<Int, Int>().apply {
        for (prime in listOf(2, 3, 5, 7, 9, 11, 13, 17, 19, 23)) set(prime, this@toItem % prime)
    })

    data class Monkey(val items: MutableList<Item>, val worry: (Item) -> Item, val test: Triple<Int, Int, Int>) {
        fun test(item: Item, useInitial: Boolean) = if (item.rem(test.first, useInitial)) test.second else test.third
    }

    fun loadMonkeys() = input.split("\n\n").map { it.split("\n") }.map { lines ->
        val items = lines[1].split(" ", ",").mapNotNull(String::toIntOrNull).toMutableList()
        val opChar = lines[2][lines[2].lastIndexOf(" ") - 1]
        val operand = lines[2].substringAfterLast(" ").toIntOrNull()
        val operation: (Item) -> Item = when (opChar) {
            '+' -> { old -> old + operand!! }
            else -> { old -> if (operand == null) old.squared() else old * operand }
        }

        val divBy = lines[3].split(" ").mapNotNull(String::toIntOrNull).single()
        val monkeyIfTrue = lines[4].split(" ").mapNotNull(String::toIntOrNull).single()
        val monkeyIfFalse = lines[5].split(" ").mapNotNull(String::toIntOrNull).single()
        Monkey(items.map { it.toItem() }.toMutableList(), operation, Triple(divBy, monkeyIfTrue, monkeyIfFalse))
    }

    fun doMonkeyBusiness(rounds: Int, div: Boolean): Long {
        val monkeys = loadMonkeys()
        val inspections = LongArray(monkeys.size) { 0 }
        repeat(rounds) {
            for ((i, monkey) in monkeys.withIndex()) with(monkey) {
                inspections[i] = inspections[i] + items.size
                for (item in items) worry(item)
                    .let { if (div) it / 3 else it }
                    .also { monkeys[test(it, div)].items += it }
                items.clear()
            }
        }
        return inspections.sortedDescending().take(2).reduce(Long::times)
    }

    return listOf(doMonkeyBusiness(20, true), doMonkeyBusiness(10_000, false))
}