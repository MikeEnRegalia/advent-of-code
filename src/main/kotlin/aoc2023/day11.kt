package aoc2023

fun main() = day11(generateSequence(::readlnOrNull).toList()).forEach(::println)

private fun day11(space: List<String>): List<Any?> {

    data class Galaxy(val x: Int, val y: Int)

    val allGalaxies = space.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c -> if (c == '#') Galaxy(x, y) else null }
    }.toSet()

    val doubleRows = space.indices.filter { y -> space[y].all { it == '.' } }
    val doubleColumns = space[0].indices.filter { x -> space.map { it[x] }.all { it == '.' } }

    var part1 = 0L
    var part2 = 0L

    (allGalaxies * allGalaxies).forEach { pair ->
        val rx = pair.map(Galaxy::x).sorted().let { (a, b) -> a until b }
        val ry = pair.map(Galaxy::y).sorted().let { (a, b) -> a until b }

        val dx = doubleColumns.count { it in rx }
        val dy = doubleRows.count { it in ry }

        val dist = rx.size() + ry.size()

        part1 += dist + dx + dy
        part2 += dist + 999999 * (dx + dy)

    }

    return listOf(part1, part2)
}

fun IntRange.size() = last - first + 1L

operator fun <T> Set<T>.times(set: Set<T>): Set<Set<T>> =
    asSequence().flatMap { a -> mapNotNull { b -> if (a == b) null else setOf(a, b) } }.toSet()