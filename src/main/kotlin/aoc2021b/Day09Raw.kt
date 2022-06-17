package aoc2021b

fun main() = generateSequence(::readLine).day09().forEach(::println)

fun Sequence<String>.day09(): Iterable<Any?> {
    data class Point(val x: Int, val y: Int) {
        fun adj() = listOf(
            Point(x - 1, y), Point(x + 1, y),
            Point(x, y - 1), Point(x, y + 1)
        )
    }

    val area = flatMapIndexed { y, row ->
        row.mapIndexed { x, c -> Point(x, y) to c.toString().toInt() }
    }.toMap()

    val lowPoints = area.entries.filter { (p, h) ->
        p.adj().all { area[it]?.let { it > h } ?: true }
    }

    val part1 = lowPoints.sumOf { it.value + 1 }

    val basins = lowPoints.map { it.key }.map { lp ->
        fun grow(basin: Set<Point>): Set<Point> {
            val grown = basin.fold(basin) { acc, p ->
                val ph = area[p]!!
                acc.plus(p.adj().filter { adj ->
                    area[adj]?.let { ah -> ah in (ph..8) } ?: false
                })
            }
            return if (grown.size > basin.size) grow(grown) else grown
        }
        grow(setOf(lp))
    }.sortedByDescending { it.size }

    val part2 = basins.take(3).fold(1) { acc, x -> acc * x.size }

    return listOf(part1, part2)
}