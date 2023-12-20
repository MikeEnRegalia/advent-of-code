package aoc2023

fun main() = day07(generateSequence(::readlnOrNull).toList()).forEach(::println)

private fun day07(lines: List<String>): List<Any?> {
    val cards = "AKQJT98765432".reversed()
    val cards2 = "AKQT98765432J".reversed()

    fun String.jokers() = count { it == 'J' }

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

    fun String.rankPart1() = when {
        isFive() -> 6
        isFour() -> 5
        isFullHouse() -> 4
        isThree() -> 3
        isTwoPairs() -> 2
        isOnePair() -> 1
        else -> 0
    }

    fun String.compareHand(other: String): Int {
        val byRank = other.rankPart1() - rankPart1()
        if (byRank != 0) return byRank

        for (i in indices) {
            val a = cards.indexOf(this[i])
            val b = cards.indexOf(other[i])
            val c = b - a
            if (c != 0) return c
        }
        return 0
    }

    fun String.withJoker() = filter { it != 'J' }
        .groupingBy { it }.eachCount().let { dist ->
            if (dist.isEmpty()) this else
                replace('J', dist.entries.maxByOrNull { it.value }!!.key)
        }

    fun String.compareHandPart2(other: String): Int {

        val byRank = other.withJoker().rankPart1() - withJoker().rankPart1()
        if (byRank != 0) return byRank

        for (i in indices) {
            val a = cards2.indexOf(this[i])
            val b = cards2.indexOf(other[i])
            val c = b - a
            if (c != 0) return c
        }
        return 0
    }

    data class Hand(val cards: String, val bid: Int)

    val hands = lines.map { it.split(" ").let { Hand(it[0], it[1].toInt()) } }
    val part1 = hands.sortedWith { a, b -> b.cards.compareHand(a.cards) }
        .mapIndexed { i, hand -> (i + 1) * hand.bid }
        .sum()
    val part2 = hands.sortedWith { a, b -> b.cards.compareHandPart2(a.cards) }
        .mapIndexed { i, hand -> (i + 1) * hand.bid }
        .sum()


    return listOf(part1, part2)
}