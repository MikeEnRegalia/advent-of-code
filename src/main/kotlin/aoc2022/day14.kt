package aoc2022

import kotlin.math.max
import kotlin.math.min

fun main() {
    val rock = generateSequence(::readlnOrNull).flatMap { row ->
        row.split(" -> ").map { it.split(",").map(String::toInt) }.windowed(2).flatMap { (from, to) ->
            (min(from[0], to[0])..max(from[0], to[0])).flatMap { x ->
                (min(from[1], to[1])..max(from[1], to[1])).map { y -> Pair(x, y) }
            }
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
        if (s.second == maxY + 1 && !reachedFloor) {
            reachedFloor = true
            println(stableSand.size)
        }
    }

    println(stableSand.size)
}

