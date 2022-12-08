package aoc2022

fun main() = day00(String(System.`in`.readAllBytes())).forEach(::println)

private fun day00(input: String): List<Any?> {

    data class Pos(val x: Int, val y: Int)

    val lines = input.lines()
    val heights = lines.flatMapIndexed { y, l ->
        l.mapIndexed { x, h -> Pos(x, y) to h.toString().toInt() }
    }.toMap()

    val part1 = heights.count { (pos, height) ->
        (0 until pos.x).all { heights.getValue(Pos(it, pos.y)) < height } ||
                (pos.x + 1 until lines[0].length).all { heights.getValue(Pos(it, pos.y)) < height } ||
                (0 until pos.y).all { heights.getValue(Pos(pos.x, it)) < height } ||
                (pos.y + 1 until lines.size).all { heights.getValue(Pos(pos.x, it)) < height }
    }

    fun List<Pos>.countViewable(ownHeight: Int): Long {
        var count = 0L
        for (pos in this) {
            val posHeight = heights.getValue(pos)
            count++
            if (posHeight >= ownHeight) return count
        }
        return count
    }

    val part2 = heights.keys.maxOf { pos ->
        val left = (0 until pos.x).reversed().map { Pos(it, pos.y) }
        val right = (pos.x + 1 until lines[0].length).map { Pos(it, pos.y) }
        val up = (0 until pos.y).reversed().map { Pos(pos.x, it) }
        val down = (pos.y + 1 until lines.size).map { Pos(pos.x, it) }

        listOf(up, left, down, right)
            .map { it.countViewable(heights.getValue(pos)) }
            .also { if (pos.x == 2 && pos.y == 3) println("$it") }
            .reduce { a, b -> a * b }
    }
    return listOf(part1, part2)
}
