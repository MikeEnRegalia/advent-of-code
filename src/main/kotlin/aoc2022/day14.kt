package aoc2022

import kotlin.math.max
import kotlin.math.min

fun main() {
    infix fun Int.range(i: Int) = min(this, i)..max(this, i)
    val rock = generateSequence(::readlnOrNull).flatMap { row ->
        row.split(" -> ", ",").map(String::toInt).windowed(4, 2).flatMap { (x1, y1, x2, y2) ->
            if (x1 == x2) (y1 range y2).map { Pair(x1, it) } else (x1 range x2).map { Pair(it, y1) }
        }
    }.toSet()

    val maxY = rock.maxOf { it.second }
    val source = Pair(500, 0)
    fun Pair<Int, Int>.flowTo() = let { (x, y) -> sequenceOf(x to y + 1, x - 1 to y + 1, x + 1 to y + 1) }
        .filterNot { it in rock || it.second >= maxY + 2 }

    val stableSand = mutableSetOf<Pair<Int, Int>>()

    var reachedFloor = false
    var s: Pair<Int, Int> = source
    while (source !in stableSand) {
        s = s.flowTo().firstOrNull { it !in stableSand } ?: source.also { stableSand += s }
        if (s.second == maxY + 1 && !reachedFloor) println(stableSand.size).also { reachedFloor = true }
    }

    println(stableSand.size)
}

