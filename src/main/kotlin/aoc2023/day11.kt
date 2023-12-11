package aoc2023

fun main() {
    val space = generateSequence(::readlnOrNull).toList()

    data class Galaxy(val x: Int, val y: Int)

    val doubleRows = space.indices.filter { y -> space[y].all { it == '.' } }
    val doubleColumns = space[0].indices.filter { x -> space.map { it[x] }.all { it == '.' } }

    fun List<String>.findGalaxies() = flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c -> if (c == '#') Galaxy(x, y) else null }
    }

    var part1 = 0L
    var part2 = 0L

    space.findGalaxies().let { it * it }.forEach { pair ->
        val rx = pair.map(Galaxy::x).sorted().let { (a, b) -> a until b }
        val ry = pair.map(Galaxy::y).sorted().let { (a, b) -> a until b }

        val doubles = doubleRows.count { it in ry } + doubleColumns.count { it in rx }

        val dist = rx.size() + ry.size()

        part1 += dist + doubles
        part2 += dist + 999999 * doubles
    }

    listOf(part1, part2).forEach(::println)
}

fun IntRange.size() = last - first + 1

operator fun <T> Collection<T>.times(set: Collection<T>): Set<Set<T>> =
    asSequence().flatMap { a -> mapNotNull { b -> if (a == b) null else setOf(a, b) } }.toSet()