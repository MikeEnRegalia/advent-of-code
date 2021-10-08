package y2018.day12

typealias Pots = Map<Int, Boolean>

fun Pots.simulateGrowth(rules: List<Pair<Pots, Boolean>>, generations: Long = 20): Long {
    var pots = this

    for (generation in 0 until generations) {
        val newPots = pots.toMutableMap()

        for (potIndex in (pots.min() - 2)..(pots.max() + 2)) {
            rules.find { (map) ->
                map.entries.all { (ruleIndex, state) -> state == (pots[potIndex + ruleIndex] ?: false) }
            }?.let { (_, grow) -> if (grow) newPots[potIndex] = true else newPots.remove(potIndex) }
        }

        if (newPots.signature() == pots.signature())
            return pots.score() + (generations - generation) * (newPots.score() - pots.score())

        pots = newPots
    }

    return pots.score().toLong()
}

private fun Pots.score() = keys.sumOf { it }
private fun Pots.min() = keys.minOf { it }
private fun Pots.max() = keys.maxOf { it }
private fun Pots.signature() = (min()..max()).map { this[it] == true }
