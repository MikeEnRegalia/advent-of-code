package aoc2022

import util.toJsonList

fun main() = day13(String(System.`in`.readAllBytes())).forEach(::println)

private fun day13(input: String): List<Any?> {
    fun Any?.clean(): Any = when (this) {
        is Double -> toInt()
        is List<*> -> map { it?.clean() }
        else -> throw IllegalStateException()
    }

    val data = input.split("\n\n").map {
        it.split("\n").map { packet ->
            packet.toJsonList().clean()
        }.let { it[0] to it[1] }
    } as List<Pair<List<*>, List<*>>>

    fun compare(a: List<*>, b: List<*>, pos: Int = 0): Boolean? {
        val left = a.elementAtOrNull(pos)
        val right = b.elementAtOrNull(pos)
        return when {
            left == null && right == null -> null
            left == null -> true
            right == null -> false
            left is Int && right is Int -> if (left == right) compare(a, b, pos + 1) else left < right
            left is List<*> && right is List<*> -> compare(left, right) ?: compare(a, b, pos+1)
            left is Int && right is List<*> -> compare(listOf(left), right) ?: compare(a, b, pos+1)
            left is List<*> && right is Int -> compare(left, listOf(right)) ?: compare(a, b, pos+1)
            else -> throw IllegalStateException("$left $right")
        }
    }

    val part1 = data.mapIndexedNotNull { i, it -> if (compare(it.first, it.second)!!) i + 1 else null }.sum()

    val divider1 = listOf(listOf(2))
    val divider2 = listOf(listOf(6))

    val dataPart2 = data.flatMap { it.toList() }.plus(listOf(divider1, divider2))
        .sortedWith { a, b -> compare(a, b).let { if (it == null) 0 else if (it) -1 else 1 } }

    val i1 = dataPart2.indexOf(divider1) + 1
    val i2 = dataPart2.indexOf(divider2) + 1
    return listOf(part1, i1 * i2)
}

