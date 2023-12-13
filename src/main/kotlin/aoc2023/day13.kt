package aoc2023

import kotlin.math.min

fun main() {
    fun List<String>.findH(skip: Int? = null) = indices.drop(1).filter { y ->
        val toTake = min(y, size - y)
        val above = drop(y - toTake).take(toTake)
        val below = drop(y).take(toTake).reversed()
        above == below
    }.firstOrNull { skip == null || it != skip }

    fun List<String>.findV(skip: Int? = null) = first().indices.drop(1).filter { x ->
        val toTake = min(x, first().length - x)
        val before = first().indices.drop(x - toTake).take(toTake).map { x1 -> map { it[x1] } }
        val after = first().indices.drop(x).take(toTake).map { x1 -> map { it[x1] } }.reversed()
        before == after
    }.firstOrNull { skip == null || it != skip }

    fun List<Pair<Int?, Int?>>.summarize() = fold(0) { acc, (v, h) -> acc + (v ?: 0) + 100 * (h ?: 0) }

    val data = String(System.`in`.readAllBytes()).split("\n\n").map { it.split("\n") }

    println(data.map { it.findV() to it.findH() }.summarize())

    fun List<String>.variations(): Sequence<List<String>> = indices.asSequence().flatMap { y ->
        first().indices.map { x ->
            mapIndexed { y2, row ->
                if (y != y2) row else row.mapIndexed { x2, c ->
                    if (x != x2) c else if (c == '.') '#' else '.'
                }.joinToString("")
            }
        }
    }

    val part2 = data.map { smudged ->
        val oldV = smudged.findV()
        val oldH = smudged.findH()

        val v = smudged.variations().mapNotNull { it.findV(skip = oldV) }.firstOrNull()
        val h = smudged.variations().mapNotNull { it.findH(skip = oldH) }.firstOrNull()

        v to h
    }

    println(part2.summarize())
}