package aoc2023

fun main() = day03(String(System.`in`.readAllBytes()).split("\n")).forEach(::println)

private fun day03(lines: List<String>): List<Any?> {
    data class Pos(val x: Int, val y: Int)

    fun Pos.neighbors() = (-1..1).asSequence()
        .flatMap { y -> (-1..1).map { x -> Pos(x, y) } }
        .map { Pos(x + it.x, y + it.y) }
        .filterNot { it.x == x && it.y == y }
        .filter { it.x in (lines.first().indices) }
        .filter { it.y in lines.indices }.toList()


    var sum = 0L

    val numbers = mutableMapOf<Pos, Long>()

    val adjacentGears = mutableMapOf<Pos, Set<Pos>>()

    for (y in lines.indices) {
        val row = lines[y]

        var x = 0
        while (x in row.indices) {
            if (row[x].isDigit()) {
                val n = row.drop(x).takeWhile { it.isDigit() }
                val symbols = n.indices.flatMap {
                    Pos(x + it, y).neighbors()
                }

                val adjacentSymbols = symbols.filter { neighbor ->
                    val c = lines[neighbor.y][neighbor.x]
                    !(c.isDigit() || c == '.')
                }

                if (adjacentSymbols.isNotEmpty()) {
                    sum += n.toLong()

                    adjacentSymbols.forEach {
                        if (lines[it.y][it.x] == '*') {
                            numbers[Pos(x, y)] = n.toLong()
                            adjacentGears.compute(it) { _, old ->
                                (old ?: setOf()) + Pos(x, y)
                            }
                        }
                    }
                }

                x += n.length
            } else x++
        }
    }

    println(adjacentGears)

    val part2 = adjacentGears.filterValues { it.size == 2 }
        .values.sumOf { it.map { numbers.getValue(it) }.reduce(Long::times) }
    return listOf(sum, part2)
}