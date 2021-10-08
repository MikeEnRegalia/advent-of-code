package y2018.day12

fun Set<Int>.simulateGrowth(rules: List<Pair<Set<Int>, Boolean>>, turns: Long): Long {
    var pots = this as Iterable<Int>
    for (turnsLeft in (1..turns).reversed()) {
        (pots.minOf { it } - 2..pots.maxOf { it } + 2).asSequence()
            .mapNotNull { rules.match(pots, it) }.toSet()
            .also {
                if (it.shape() == pots.shape()) return pots.score() + turnsLeft * (it.score() - pots.score())
                else pots = it
            }
    }
    return pots.score()
}

private fun List<Pair<Set<Int>, Boolean>>.match(pots: Iterable<Int>, index: Int) =
    firstOrNull { (-2..2).all { d -> it.first.contains(d) == pots.contains(index + d) } }
        ?.takeIf { it.second }?.let { index }

private fun Iterable<Int>.score() = sumOf { it }.toLong()
private fun Iterable<Int>.shape() = (minOf { it }..maxOf { it }).map { contains(it) }
