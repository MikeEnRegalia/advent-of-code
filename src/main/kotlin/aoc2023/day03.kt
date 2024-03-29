package aoc2023

fun main() {
    val lines = generateSequence(::readLine).toList()

    data class Pos(val x: Int, val y: Int)

    fun Pos.neighbors() = (-1..1).asSequence()
        .flatMap { y -> (-1..1).map { x -> Pos(this.x + x, this.y + y) } }
        .filterNot { it.x == x && it.y == y }
        .filter { it.y in lines.indices && it.x in lines[y].indices }
        .toList()

    var part1 = 0L
    val numbers = mutableMapOf<Pos, Long>()
    val gears = mutableMapOf<Pos, Set<Pos>>()
    for ((y, row) in lines.withIndex<String>()) {
        var x = 0
        while (x in row.indices) {
            if (!row[x].isDigit()) x++
            else {
                val n = row.drop(x).takeWhile(Char::isDigit)
                val neighbors = n.indices.flatMap { Pos(x + it, y).neighbors() }

                val symbols = neighbors.filter { neighbor ->
                    val c = lines[neighbor.y][neighbor.x]
                    !(c.isDigit() || c == '.')
                }

                if (symbols.isNotEmpty<Pos>()) {
                    part1 += n.toLong()

                    symbols.filter<Pos> { lines[it.y][it.x] == '*' }.forEach<Pos> {
                        numbers[Pos(x, y)] = n.toLong()
                        gears.compute(it) { _, old -> (old ?: setOf()) + Pos(x, y) }
                    }
                }

                x += n.length
            }
        }
    }
    val part2 = gears
        .filterValues { it.size == 2 }
        .values.sumOf { it.map(numbers::getValue).reduce(Long::times) }
    listOf(part1, part2).forEach(::println)
}

