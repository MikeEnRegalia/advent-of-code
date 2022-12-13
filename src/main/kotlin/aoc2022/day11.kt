package aoc2022

fun main() {
    data class Item(private val initial: Int, private val remainders: Map<Int, Int>) {
        fun rem(d: Int, useInitial: Boolean) = 0 == if (useInitial) (initial % d) else remainders.getValue(d)
        fun transform(op: (Int) -> Int) = Item(op(initial), remainders.toMutableMap().apply {
            keys.forEach { prime -> set(prime, op(getValue(prime)) % prime) }
        })
    }

    fun Int.toItem() = Item(this, buildMap {
        for (prime in listOf(2, 3, 5, 7, 9, 11, 13, 17, 19, 23)) set(prime, this@toItem % prime)
    })

    data class Monkey(val items: MutableList<Item>, val operation: (Item) -> Item, val test: Triple<Int, Int, Int>) {
        fun test(item: Item, useInitial: Boolean) = if (item.rem(test.first, useInitial)) test.second else test.third
    }

    val input = System.`in`.reader().readText()
    fun doMonkeyBusiness(rounds: Int, div: Boolean): Long {
        val monkeys = input.split("\n\n").map { it.split("\n") }.map { lines ->
            val items = lines[1].split(" ", ",").mapNotNull(String::toIntOrNull).map { it.toItem() }.toMutableList()
            val opChar = lines[2][lines[2].lastIndexOf(" ") - 1]
            val operand = lines[2].substringAfterLast(" ").toIntOrNull()
            val operation: (Item) -> Item = when {
                opChar == '+' -> { old -> old.transform { it + operand!! } }
                operand != null -> { old -> old.transform { it * operand } }
                else -> { old -> old.transform { it * it } }
            }

            val divBy = lines[3].split(" ").mapNotNull(String::toIntOrNull).single()
            val ifTrue = lines[4].split(" ").mapNotNull(String::toIntOrNull).single()
            val ifFalse = lines[5].split(" ").mapNotNull(String::toIntOrNull).single()
            Monkey(items, operation, Triple(divBy, ifTrue, ifFalse))
        }

        val inspections = LongArray(monkeys.size) { 0 }
        repeat(rounds) {
            for ((i, monkey) in monkeys.withIndex()) with(monkey) {
                inspections[i] = inspections[i] + items.size
                for (item in items) operation(item)
                    .let { newItem -> if (div) newItem.transform { it / 3 } else newItem }
                    .also { monkeys[test(it, div)].items += it }
                items.clear()
            }
        }
        return inspections.sortedDescending().take(2).reduce(Long::times)
    }
    listOf(doMonkeyBusiness(20, true), doMonkeyBusiness(10_000, false)).forEach(::println)
}

