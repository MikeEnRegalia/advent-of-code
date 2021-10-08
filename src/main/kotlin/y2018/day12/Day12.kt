package y2018.day12

fun Map<Int, Boolean>.simulateGrowth(rules: List<Pair<Map<Int, Boolean>, Boolean>>, turns: Long): Long {
    var pots = this
    for (turnsLeft in (1..turns).reversed()) {
        (pots.min() - 2..pots.max() + 2)
            .mapNotNull { index ->
                rules.find { it.first.entries.all { (d, state) -> state == (pots[index + d] ?: false) } }
                    ?.let { index to it.second }
            }
            .fold(pots.toMutableMap()) { newPots, (i, grow) -> newPots.store(i, grow) }
            .also {
                if (it.signature() == pots.signature())
                    return pots.score() + turnsLeft * (it.score() - pots.score())
            }
            .also { pots = it }
    }
    return pots.score().toLong()
}

private fun MutableMap<Int, Boolean>.store(i: Int, grow: Boolean) = apply { if (grow) this[i] = true else remove(i) }
private fun Map<Int, Boolean>.score() = keys.sumOf { it }
private fun Map<Int, Boolean>.min() = keys.minOf { it }
private fun Map<Int, Boolean>.max() = keys.maxOf { it }
private fun Map<Int, Boolean>.signature() = (min()..max()).map { this[it] == true }
