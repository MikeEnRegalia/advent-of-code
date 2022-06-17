package aoc2021b

fun main() = generateSequence(::readLine).day09Immutable().forEach(::println)

fun Sequence<String>.day09Immutable(): Iterable<Any?> {
    data class Location(val x: Int, val y: Int, val height: Int)
    val area = flatMapIndexed { y, row -> row.mapIndexed { x, c -> Location(x, y, c.toString().toInt()) } }.toSet()

    val adj = setOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
    fun Set<Location>.adj() = area.filter { (x, y) -> any { p -> (x to y) in adj.map { (dx, dy) -> p.x + dx to p.y + dy } } }.toSet()

    val lowPoints = area.filter { p -> setOf(p).adj().all { a -> a.height > p.height } }
    val part1 = lowPoints.sumOf { it.height + 1 }

    fun flood(points: Set<Location>): Set<Location> {
        val new = points.adj().filter { it.height != 9 }.toSet().minus(points)
        return if (new.isEmpty()) points else flood(points.plus(new))
    }

    val basins = lowPoints.map { flood(setOf(it)) }
    val part2 = basins.sortedByDescending { it.size }.take(3).map { it.size }.reduce { a, b -> a * b }

    return listOf(part1, part2)
}