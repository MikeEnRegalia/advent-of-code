package y2018.day12

typealias Pots = Iterable<Int>
typealias Rules = Iterable<Pair<Iterable<Int>, Boolean>>

fun Pots.simulateGrowth(rules: Rules, turns: Long): Long {
    var pots = this
    for (turnsLeft in turns downTo 1)
        pots = pots.widerRange().filter { it.growsWith(pots, rules) }.also { nextPots ->
            if (nextPots.shape() == pots.shape()) return pots.score() + turnsLeft * (nextPots.score() - pots.score())
        }
    return pots.score()
}

private fun Pots.widerRange() = minOf { it } - 2..maxOf { it } + 2
private fun Int.growsWith(pots: Pots, rules: Rules) =
    rules.firstOrNull { (rule) -> rule.matches(pots, this) }?.second ?: false

private fun Iterable<Int>.matches(pots: Pots, p: Int) = (-2..2).all { i -> (i in this) == ((p + i) in pots) }
private fun Pots.score() = sumOf { it }.toLong()
private fun Pots.shape() = (minOf { it }..maxOf { it }).map { contains(it) }