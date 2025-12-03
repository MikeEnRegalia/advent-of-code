package aoc2025

fun main() {
    val banks = generateSequence(::readLine).toList()

    fun String.pick(pos: Int, picked: List<Long>, n: Int): Long {
        if (n == 0) return picked.joinToString("").toLong()
        val candidates = (pos..indices.last - (n - 1)).toList()
        val max = candidates.maxOf { this[it].digitToInt() }
        return candidates
            .filter { this[it].digitToInt() == max }
            .maxOf { pick(it + 1, picked + this[it].digitToInt().toLong(), n - 1) }
    }

    println(banks.sumOf { it.pick(0, listOf(), 2) })
    println(banks.sumOf { it.pick(0, listOf(), 12) })

}
