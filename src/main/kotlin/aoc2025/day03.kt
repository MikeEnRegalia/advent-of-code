package aoc2025

fun main() {
    val banks = generateSequence(::readLine)
        .map { it.map(Char::digitToInt) }
        .toList()

    fun List<Int>.solve(n: Int): Long {
        var curr = listOf<Int>()

        fun List<Int>.pick(n: Int, pos: Int = 0, picked: List<Int> = listOf()): List<Int>? {
            if (n == 0) return picked

            val candidates = (pos..indices.last - (n - 1)).toList()
            val max = candidates.maxOf { this[it] }

            if (picked.size in curr.indices && curr[picked.size] >= max) return null

            return candidates
                .filter { this[it] == max }
                .mapNotNull { pick(n - 1,it + 1, picked + max) }
                .maxByOrNull { it.joinToString("").toLong() }
                ?.also { curr = it }
        }

        return pick(n)!!.joinToString("").toLong()
    }

    println(banks.sumOf { it.solve(2) })
    println(banks.sumOf { it.solve(12) })

}
