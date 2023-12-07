package aoc2023

fun main() = day07(generateSequence(::readlnOrNull).toList()).forEach(::println)

private fun day07(lines: List<String>): List<Any?> {
    val cards = "AKQJT98765432".reversed()

    fun String.isFive() = groupingBy { it }.eachCount().size == 1
    fun String.isFour() = groupingBy { it }.eachCount().let {
        it.size == 2 && it.values.any { it == 1 }
    }

    fun String.isFullHouse() = groupingBy { it }.eachCount().let {
        it.size == 2 && it.values.any { it == 3 }
    }

    fun String.isThree() = groupingBy { it }.eachCount().let {
        it.size == 3 && it.values.any { it == 3 }
    }

    fun String.isTwoPairs() = groupingBy { it }.eachCount().let {
        it.size == 3 && it.values.any { it == 1 }
    }

    fun String.isOnePair() = groupingBy { it }.eachCount().let {
        it.size == 4
    }

    fun String.rank() = when {
        isFive() -> 6
        isFour() -> 5
        isFullHouse() -> 4
        isThree() -> 3
        isTwoPairs() -> 2
        isOnePair() -> 1
        else -> 0
    }

    fun String.compareHand(other: String): Int {
        val byRank = other.rank() - rank()
        if (byRank != 0) return byRank

        for (i in indices) {
            val a = cards.indexOf(this[i])
            val b = cards.indexOf(other[i])
            val c = b - a
            if (c != 0) return c
        }
        return 0
    }

    data class Hand(val cards: String, val bid: Int)

    val hands = lines.map { it.split(" ").let { Hand(it[0], it[1].toInt()) } }
    val part1 = hands.sortedWith { a, b -> b.cards.compareHand(a.cards) }
        .also { println(it.map { it.cards }) }
        .mapIndexed { i, hand -> (i + 1) * hand.bid }
        .sum()
    return listOf(part1)
}