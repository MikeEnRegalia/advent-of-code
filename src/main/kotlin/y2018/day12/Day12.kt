package y2018.day12

typealias Pots = Map<Int, Boolean>
typealias Rule = Pair<Pots, Boolean>

fun Pots.simulateGrowth(rules: List<Rule>, generations: Long = 20): Long {
    var pots = this

    for (generation in 0 until generations) {
        val newPots = pots.toMutableMap()
        val (min, max) = pots.keys.minOf { it } to pots.keys.maxOf { it }
        for (index in (min - 2)..(max + 2)) {
            rules.find { (map) ->
                map.entries.all { (ruleIndex, state) ->
                    (pots[index + ruleIndex] ?: false) == state
                }
            }?.let { (_, grow) ->
                if (grow) {
                    newPots[index] = true
                } else newPots.remove(index)
            }
        }

        if (newPots.signature() == pots.signature())
            return pots.score() + (generations - generation) * (newPots.score() - pots.score())

        pots = newPots
    }

    return pots.score().toLong()
}

private fun Pots.score() = keys.sumOf { it }
private fun Pots.signature() = (keys.minOf { it }..keys.maxOf { it }).map { this[it] == true }
