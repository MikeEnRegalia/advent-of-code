package y2018.day12

fun Map<Int, Boolean>.simulateGrowth(rules: List<Pair<Map<Int, Boolean>, Boolean>>, turns: Long): Long {
    var pots = this
    for (turnsLeft in (1..turns).reversed()) {
        (pots.min() - 2..pots.max() + 2)
            .mapNotNull { rules.match(pots, it) }
            .associateWith { true }
            .also {
                if (it.shape() == pots.shape()) return pots.score() + turnsLeft * (it.score() - pots.score())
                else pots = it
            }
    }
    return pots.score()
}

private fun List<Pair<Map<Int, Boolean>, Boolean>>.match(pots: Map<Int, Boolean>, index: Int) =
    find { it.first.entries.all { (d, state) -> state == (pots[index + d] ?: false) } }
        ?.takeIf { it.second }?.let { index }

private fun Map<Int, Boolean>.score() = keys.sumOf { it }.toLong()
private fun Map<Int, Boolean>.min() = keys.minOf { it }
private fun Map<Int, Boolean>.max() = keys.maxOf { it }
private fun Map<Int, Boolean>.shape() = (min()..max()).map { this[it] == true }
