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

    val adjacentGears = mutableMapOf<Pos, Pos>()

    for (y in lines.indices) {
        val row = lines[y]

        var x = 0
        while (x in row.indices) {
            if (row[x].isDigit()) {
                val n = row.drop(x).takeWhile { it.isDigit() }
                if (n.indices.any {
                        Pos(x + it, y).neighbors().any { neighbor ->
                            val c = lines[neighbor.y][neighbor.x]
                            !(c.isDigit() || c == '.')
                        }
                    }) {
                    sum += n.toLong()
                }
                x += n.length
            } else x++
        }
    }
    return listOf(sum)
}