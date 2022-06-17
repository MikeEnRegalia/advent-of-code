package aoc2021b

import kotlin.math.abs

fun main() {
    data class Pos(val x: Int, val y: Int) {
        fun adj() = sequenceOf(Pos(x - 1, y), Pos(x + 1, y), Pos(x, y - 1), Pos(x, y + 1))
        fun dist(to: Pos = Pos(0, 0)) = abs(x - to.x) + abs(y - to.y)
    }

    fun findPath(cave: Map<Pos, Int>, from: Pos, to: Pos): Int? {
        val height = cave.keys.maxOf { it.y } + 1
        val width = cave.keys.maxOf { it.x } + 1
        val cache = Array(height) { IntArray(width) { Integer.MAX_VALUE } }
        var paths = setOf(listOf(from))

        fun List<Pos>.score() = drop(1).map { cave[it]!! }.sumOf { it }

        while (true) {
            val newPaths = paths.asSequence().filter { it.last() != to }
                .flatMap { path ->
                    path.last().adj().filter {
                        it in cave && path.dropLast(1).none { p -> p.dist(it) <= 1 }
                    }.map { path.plus(it) }
                }
                .filter { path ->
                    val score = path.score()
                    val min = cache[path.last().y][path.last().x]
                    if (score < min) {
                        cache[path.last().y][path.last().x] = score
                        true
                    } else false
                }
                .toSet()
                .let { allNewPaths ->
                    allNewPaths.map { it.last() }.distinct().mapNotNull { last ->
                        allNewPaths.filter { it.last() == last }
                            .minByOrNull { it.drop(1).map { cave[it]!! }.sumOf { it } }
                    }
                }.toSet()

            if (newPaths.isEmpty()) break
            else paths = newPaths
        }
        return cache[to.y][to.x].takeUnless { it == Integer.MAX_VALUE }
    }

    val smallCave = generateSequence(::readLine).flatMapIndexed { y, row ->
        row.mapIndexed { x, c -> Pos(x, y) to c.digitToInt() }
    }.toMap()

    val bigCave = buildMap {
        val xSize = smallCave.keys.maxOf { it.x } + 1
        val ySize = smallCave.keys.maxOf { it.y } + 1
        for (x in 0..4) {
            for (y in 0..4) {
                smallCave.entries.forEach { (cavePos, risk) ->
                    set(
                        Pos(x * xSize + cavePos.x, y * ySize + cavePos.y),
                        (risk + x + y).let { if (it >= 10) it - 9 else it }
                    )
                }
            }
        }
    }

    sequenceOf(smallCave, bigCave).map { cave ->
        findPath(cave, from = Pos(0, 0), to = Pos(cave.keys.maxOf { it.x }, cave.keys.maxOf { it.y }))
    }.forEach(::println)
}

