package aoc2025

fun main() {
    val banks = generateSequence(::readLine).map { it.map(Char::digitToInt) }.toList()

    fun List<Int>.solve(n: Int): Long {
        var solution = listOf<Int>()

        fun List<Int>.pick(n: Int, pos: Int = 0, picked: List<Int> = listOf()): List<Int>? {
            if (n == 0) return picked

            val candidates = (pos..indices.last - (n - 1)).toList()
            val max = candidates.maxOf { this[it] }

            if (picked.size in solution.indices && solution[picked.size] >= max) return null

            return candidates
                .filter { this[it] == max }
                .mapNotNull { pick(n - 1, it + 1, picked + max) }
                .maxByOrNull { it.joinToString("").toLong() }
                ?.also { solution = it }
        }

        pick(n)

        return solution.joinToString("").toLong()
    }

    listOf(2, 12).map { n -> banks.sumOf { it.solve(n) } }.forEach(::println)
}
