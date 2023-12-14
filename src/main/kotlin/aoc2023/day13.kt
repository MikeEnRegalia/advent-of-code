package aoc2023

import aoc2021b.splitMap
import kotlin.math.min

fun main() {
    fun List<String>.findH(skip: Int? = null) = indices.drop(1).filter { y ->
        val (above, below) = min(y, size - y).let { n ->
            drop(y - n).take(n) to drop(y).take(n).reversed()
        }
        above == below
    }.firstOrNull { skip == null || it != skip }

    fun List<String>.findV(skip: Int? = null) = first().indices.drop(1).filter { x ->
        val toTake = min(x, first().length - x)
        val before = first().indices.drop(x - toTake).take(toTake).map { x1 -> map { it[x1] } }
        val after = first().indices.drop(x).take(toTake).map { x1 -> map { it[x1] } }.reversed()
        before == after
    }.firstOrNull { skip == null || it != skip }

    fun List<Pair<Int?, Int?>>.summarize() = fold(0) { acc, (v, h) -> acc + (v ?: 0) + 100 * (h ?: 0) }

    val data = String(System.`in`.readAllBytes()).split("\n\n").map(String::lines)

    val part1 = data.map { it.findV() to it.findH() }.summarize()
    println(part1)

    fun List<String>.variations(): Sequence<List<String>> = indices.asSequence().flatMap { y ->
        first().indices.map { x ->
            mapIndexed { y2, row ->
                if (y != y2) row else row.mapIndexed { x2, c ->
                    if (x != x2) c else if (c == '.') '#' else '.'
                }.joinToString("")
            }
        }
    }

    fun List<String>.findReflection(t: (List<String>) -> Int?) = variations().mapNotNull(t).firstOrNull()

    val part2 = data.map { smudged ->
        val (oldV, oldH) = smudged.findV() to smudged.findH()
        smudged.findReflection { it.findV(skip = oldV) } to smudged.findReflection { it.findH(skip = oldH) }
    }.summarize()

    println(part2)
}