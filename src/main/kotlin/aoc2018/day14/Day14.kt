package aoc2018.day14

import java.util.LinkedList

fun init() = listOf(3, 7).toMutableList() to mutableListOf(0, 1)

fun scoreRecipesAfter(tenAfter: Int): String {
    val (scores, elves) = init()
    while (scores.size < tenAfter + 10) elves.nextRound(scores)
    return with(scores) { subList(size - 10, size).joinToString("") }
}

fun findScoreSequence(pattern: List<Int>): Int {
    val (scores, elves) = init()
    var count = scores.size
    val last = LinkedList(scores)
    while (true) {
        elves.nextRound(scores) {
            last.add(it)
            count++
            if (last.size > pattern.size) last.removeFirst()
            if (pattern == last) return count - pattern.size
        }
    }
}

private inline fun MutableList<Int>.nextRound(scores: MutableList<Int>, registerScore: (Int) -> Unit = {}) {
    sumOf { scores[it] }.digits().forEach { scores.add(it); registerScore(it) }
    replaceAll { (it + 1 + scores[it]) % scores.size }
}

internal fun Int.digits(): Iterable<Int> =
    if (this < 10) listOf(this % 10) else listOf(this / 10, this % 10)