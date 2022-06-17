package aoc2021b

fun main() = day09Crafted(generateSequence(::readLine).toList()).forEach(::println)

fun day09Crafted(input: List<String>): Iterable<Any?> {
    val area = input.map { it.map(Char::digitToInt) }

    val allPoints = area.indices.flatMap { y -> area[0].indices.map { it to y } }

    fun adj(x: Int, y: Int) = sequenceOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
        .map { (dx, dy) -> x + dx to y + dy }
        .filter { (x, y) -> x in area[0].indices && y in area.indices }

    val lowPoints = allPoints.filter { (x, y) -> adj(x, y).all { (ax, ay) -> area[ay][ax] > area[y][x] } }

    val part1 = lowPoints.sumOf { (x, y) -> area[y][x] + 1 }
    val part2 = buildList<Int> {
        for (lowPoint in lowPoints) {
            val basin = buildSet<Pair<Int, Int>> {
                add(lowPoint)
                while (true) {
                    val new = flatMap { (x, y) -> adj(x, y) }.filter { (nx, ny) -> area[ny][nx] < 9 }
                    if (!addAll(new)) break
                }
            }
            add(basin.size)
            if (size > 3) remove(minOfOrNull { it })
        }
    }.reduce(Int::times)

    return listOf(part1, part2)
}