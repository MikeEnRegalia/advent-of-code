package y2018.day12

fun Set<Int>.simulateGrowth(rules: List<Pair<Set<Int>, Boolean>>, turns: Long): Long {
    var pots = this as Iterable<Int>
    for (turnsLeft in turns downTo 1) {
        pots.indexes()
            .mapNotNull { rules.match(pots, it) }.toSet()
            .also {
                if (it.shape() == pots.shape()) return pots.score() + turnsLeft * (it.score() - pots.score())
                else pots = it
            }
    }
    return pots.score()
}

private fun Iterable<Int>.indexes() = (minOf { it } - 2..maxOf { it } + 2).asSequence()

private fun List<Pair<Set<Int>, Boolean>>.match(pots: Iterable<Int>, index: Int) =
    firstOrNull { (rule) -> (-2..2).all { i -> (i in rule) == ((index + i) in pots) } }
        ?.takeIf { it.second }?.let { index }

private fun Iterable<Int>.score() = sumOf { it }.toLong()
private fun Iterable<Int>.shape() = (minOf { it }..maxOf { it }).map { contains(it) }
