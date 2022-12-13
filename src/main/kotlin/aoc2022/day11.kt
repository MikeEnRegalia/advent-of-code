package aoc2022

import java.math.BigInteger
import java.math.BigInteger.ZERO

fun main() = day11(String(System.`in`.readAllBytes())).forEach(::println)

private fun day11(input: String): List<Any?> {
    data class Item(val level: BigInteger, val divisibilities: MutableMap<Int, Boolean> = mutableMapOf()) {
        operator fun plus(x: Int) = Item(level + x.toBigInteger())
        operator fun div(x: Int) = Item(level / x.toBigInteger())
        operator fun times(x: Int) =
            Item(level * x.toBigInteger(), divisibilities.toMutableMap().apply { set(x, true) })

        fun squared() = Item(level * level, divisibilities.toMutableMap())
        fun isDivisibleBy(x: Int) = divisibilities[x] ?: (level.remainder(x.toBigInteger()) == ZERO).also { divisibilities[x] = it }
    }

    data class Monkey(
        var items: MutableList<Item>,
        val operation: (Item) -> Item,
        val divBy: Int,
        val ifTrue: Int,
        val ifFalse: Int
    ) {
        fun test(item: Item) = if (item.isDivisibleBy(divBy)) ifTrue else ifFalse
        override fun toString() = "$items"
    }

    fun loadMonkeys() = input.split("\n\n").map { it.split("\n") }.map { lines ->
        val items = lines[1].split(" ", ",").mapNotNull(String::toIntOrNull).toMutableList()
        val opChar = lines[2][lines[2].lastIndexOf(" ") - 1]
        val operand = lines[2].substringAfterLast(" ").toIntOrNull()
        val operation: (Item) -> Item = when (opChar) {
            '+' -> { old -> old + operand!! }
            '*' -> { old -> operand?.let { old * it } ?: old.squared() }
            else -> throw IllegalArgumentException()
        }

        val divBy = lines[3].split(" ").mapNotNull(String::toIntOrNull).single()
        val monkeyIfTrue = lines[4].split(" ").mapNotNull(String::toIntOrNull).single()
        val monkeyIfFalse = lines[5].split(" ").mapNotNull(String::toIntOrNull).single()
        Monkey(items.map { Item(it.toBigInteger()) }.toMutableList(), operation, divBy, monkeyIfTrue, monkeyIfFalse)
    }

    fun doMonkeyBusiness(rounds: Int, div: Boolean): Long {
        val monkeys = loadMonkeys()
        val inspections = mutableMapOf<Int, Long>()
        repeat(rounds) { round ->
            println("$round")
            for ((i, monkey) in monkeys.withIndex()) with(monkey) {
                inspections[i] = inspections.getOrDefault(i, 0) + items.size
                items.map { operation(it)  }.forEach { monkeys[test(it)].items += it }
                items.clear()
            }
            if (!div && round in listOf(0, 19, 999)) println(inspections)
        }
        return inspections.values.sortedDescending().take(2).reduce(Long::times)
    }

    return listOf(doMonkeyBusiness(10_000, false))
}