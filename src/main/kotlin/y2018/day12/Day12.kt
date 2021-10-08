package y2018.day12

typealias Pots = Map<Int, Boolean>

fun Pots.simulateGrowth(rules: List<Pair<Pots, Boolean>>, generations: Long = 20): Long {
    var pots = this
    for (generationsLeft in (1..generations).reversed()) {
        (pots.min() - 2..pots.max() + 2)
            .mapNotNull { index ->
                rules.find { it.first.entries.all { (d, state) -> state == (pots[index + d] ?: false) } }
                    ?.let { index to it.second }
            }
            .fold(pots.toMutableMap()) { newPots, (i, grow) -> newPots.store(i, grow) }
            .also {
                if (it.signature() == pots.signature())
                    return pots.score() + generationsLeft * (it.score() - pots.score())
            }
            .also { pots = it }
    }
    return pots.score().toLong()
}

private fun MutableMap<Int, Boolean>.store(i: Int, grow: Boolean) = apply { if (grow) this[i] = true else remove(i) }
private fun Pots.score() = keys.sumOf { it }
private fun Pots.min() = keys.minOf { it }
private fun Pots.max() = keys.maxOf { it }
private fun Pots.signature() = (min()..max()).map { this[it] == true }
