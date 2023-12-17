package aoc2023

import aoc2023.Direction.*

private data class Pos(val x: Int, val y: Int)
private data class Beam(val pos: Pos, val direction: Direction)
private typealias State = Pair<Set<Beam>, Set<Beam>>

private enum class Direction { N, E, S, W }

fun main() {
    val grid = generateSequence(::readLine).toList()
    val xRange = grid[0].indices
    val yRange = grid.indices

    fun Pos.next(direction: Direction) = when (direction) {
        E -> copy(x = x + 1)
        W -> copy(x = x - 1)
        N -> copy(y = y - 1)
        S -> copy(y = y + 1)
    }.takeIf { it.x in xRange && it.y in yRange }

    fun Beam.next(): List<Beam> {
        val c = grid[pos.y][pos.x]
        val actualDirections = when (c) {
            '/' -> listOf(
                when (direction) {
                    E -> N
                    N -> E
                    W -> S
                    S -> W
                }
            )

            '\\' -> listOf(
                when (direction) {
                    E -> S
                    S -> E
                    W -> N
                    N -> W
                }
            )

            '-' -> when (direction) {
                N, S -> listOf(E, W)
                else -> listOf(direction)
            }

            '|' -> when (direction) {
                E, W -> listOf(N, S)
                else -> listOf(direction)
            }

            else -> listOf(direction)
        }

        return actualDirections.mapNotNull { dir ->
            pos.next(dir)?.let { Beam(it, dir) }
        }
    }

    fun State.next(): State {
        val (active, old) = this
        return active.flatMap { it.next() }.filter { it !in old }.toSet() to old + active
    }

    fun State.evolve(): Int {
        var curr = this
        while (true) {
            val new = curr.next()
            if (new.first.isEmpty()) break
            curr = new
        }
        return curr.second.map { it.pos }.distinct().count() + 1
    }

    val curr: State = Beam(Pos(0, 0), E).let { setOf(it) to setOf() }
    println(curr.evolve())

    val part2 = (0..yRange.last).flatMap { y ->
        (0..xRange.last).flatMap { x ->
            val dirs = when {
                x == 0 && y == 0 -> listOf(E, S)
                x == 0 && y == yRange.last -> listOf(E, N)
                x == xRange.last && y == 0 -> listOf(W, S)
                x == xRange.last && y == yRange.last -> listOf(W, N)
                x == 0 -> listOf(E)
                y == 0 -> listOf(S)
                y == yRange.last -> listOf(N)
                x == xRange.last -> listOf(W)
                else -> return@flatMap listOf()
            }
            dirs.map { dir ->
                val s: State = Beam(Pos(x, y), dir).let { setOf(it) to setOf() }
                s.evolve()
            }
        }
    }.max()
    println(part2 - 1)
}