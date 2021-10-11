package y2018.day14

import java.util.*

fun init() = listOf(3, 7).toMutableList() to mutableListOf(0, 1)

fun scoreRecipesAfter(tenAfter: Int): String {
    val (scores, elves) = init()
    with(scores) {
        while (size < tenAfter + 10) elves.nextRound(this)
        return subList(size - 10, size).joinToString("")
    }
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

private inline fun MutableList<Int>.nextRound(scoreboard: MutableList<Int>, v: (Int) -> Unit = {}) {
    sumOf { scoreboard[it] }.let { sum ->
        val first = if (sum < 10) null else sum / 10
        first?.let { scoreboard.add(it); v(it) }
        scoreboard.add((sum % 10).also { v(it) })
        replaceAll { (it + 1 + scoreboard[it]) % scoreboard.size }
    }
}