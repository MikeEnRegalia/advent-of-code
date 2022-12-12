package aoc2022

import java.math.BigInteger
import java.math.BigInteger.ZERO

fun main() = day11(String(System.`in`.readAllBytes())).forEach(::println)

private fun day11(input: String): List<Any?> {
    data class N(val r: Long, val factors: MutableSet<Long> = mutableSetOf()) {
        fun toBigInteger() = BigInteger.valueOf(r) * (if (factors.isEmpty()) BigInteger.ONE else factors.map { BigInteger.valueOf(it) }.reduce(BigInteger::times))
        fun divisibleBy(d: Int) = toBigInteger().mod(BigInteger.valueOf(d.toLong())) == ZERO
    }

    fun BigInteger.toN() = N(longValueExact())

    fun Int.big() = N(toLong())
    data class Monkey(
        var items2: MutableList<Int>,
        val operation: (BigInteger) -> BigInteger,
        val test: (BigInteger) -> Int
    ) {
        val items = items2.map { it.big() }.toMutableList()
    }

    val monkeys = listOf(
        Monkey(
            mutableListOf(74, 64, 74, 63, 53),
            { it * BigInteger.valueOf(7L) },
            { if (it.mod(BigInteger.valueOf(5L)) == ZERO) 1 else 6 }),
        Monkey(mutableListOf(69, 99, 95, 62), { it * it }, { if (it.mod(BigInteger.valueOf(17L)) == ZERO) 2 else 5 }),
        Monkey(
            mutableListOf(59, 81),
            { it + BigInteger.valueOf(8L) },
            { if (it.mod(BigInteger.valueOf(7L)) == ZERO) 4 else 3 }),
        Monkey(
            mutableListOf(50, 67, 63, 57, 63, 83, 97),
            { it + BigInteger.valueOf(4L) },
            { if (it.mod(BigInteger.valueOf(13L)) == ZERO) 0 else 7 }),
        Monkey(
            mutableListOf(61, 94, 85, 52, 81, 90, 94, 70),
            { it + BigInteger.valueOf(3L) },
            { if (it.mod(BigInteger.valueOf(19L)) == ZERO) 7 else 3 }),
        Monkey(
            mutableListOf(69),
            { it + BigInteger.valueOf(5L) },
            { if (it.mod(BigInteger.valueOf(3L)) == ZERO) 4 else 2 }),
        Monkey(
            mutableListOf(54, 55, 58),
            { it + BigInteger.valueOf(7L) },
            { if (it.mod(BigInteger.valueOf(11L)) == ZERO) 1 else 5 }),
        Monkey(
            mutableListOf(79, 51, 83, 88, 93, 76),
            { it * BigInteger.valueOf(3L) },
            { if (it.mod(BigInteger.valueOf(2L)) == ZERO) 0 else 6 })
    )


    val inspections = mutableMapOf<Int, Long>()
    repeat(10000) {
        for ((monkeyIndex, monkey) in monkeys.withIndex()) {
            inspections[monkeyIndex] = inspections.getOrDefault(monkeyIndex, 0) + monkey.items.size
            monkey.items.map { monkey.operation(it.toBigInteger()) }
                .map { it }
                .forEach {
                    monkeys[monkey.test(it)].items += it.toN()
                }
            monkey.items.clear()
        }
    }
    return listOf(inspections.values.sortedDescending().take(2).reduce(Long::times), null)
}

