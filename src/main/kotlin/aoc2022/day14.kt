package aoc2022

import kotlin.math.max
import kotlin.math.min

fun main() {
    data class Pos(val x: Int, val y: Int) {
        fun flowTo(p: (Pos) -> Boolean) =
            sequenceOf(copy(y = y + 1), copy(x = x - 1, y = y + 1), copy(x = x + 1, y = y + 1)).firstOrNull(p)
    }

    val rock = buildSet {
        generateSequence(::readlnOrNull).forEach { row ->
            row.split(" -> ").map { it.split(",").map(String::toInt) }.windowed(2).forEach { (from, to) ->
                for (x in min(from[0], to[0])..max(from[0], to[0]))
                    for (y in min(from[1], to[1])..max(from[1], to[1])) this.add(Pos(x, y))
            }
        }
    }

    val maxY = rock.maxOf { it.y }
    val floor = maxY + 2
    val source = Pos(500, 0)

    fun part1() = mutableSetOf<Pos>().also { stableSand ->
        var s: Pos = source
        while (s.y < maxY)
            s = s.flowTo { it !in rock && it !in stableSand } ?: source.also { stableSand += s }
    }

    fun part2() = mutableSetOf<Pos>().also { stableSand ->
        var s: Pos = source
        while (source !in stableSand)
            s = s.flowTo { it.y < floor && it !in rock && it !in stableSand } ?: source.also { stableSand += s }
    }

    listOf(part1().size, part2().size).forEach(::println)
}

