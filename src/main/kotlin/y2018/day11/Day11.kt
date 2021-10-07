package y2018.day11

fun day11(serial: Int, anySquare: Boolean = false): List<Int> {
    val grid = sequence {
        for (x in 1..300)
            for (y in 1..300) yield(Cell(x, y))
    }.toList()

    return sequence {
        for (size in if (anySquare) 1..300 else 3..3) {
            for (x in 1..300 - (size - 1))
                for (y in 1..300 - (size - 1)) {
                    yield(Square(grid.filter { it.x in x..x + (size - 1) && it.y in y..y + (size - 1) }, size))
                }
        }
    }.maxByOrNull { square -> square.cells.sumOf { it.power(serial) } }!!
        .let { listOf(it.cells[0].x, it.cells[0].y, it.size) }
}

internal class Square(val cells: List<Cell>, val size: Int)

internal data class Cell(val x: Int, val y: Int) {
    fun power(serial: Int): Int {
        val rackId = x + 10
        val power = (rackId * y + serial) * rackId
        val h = if (power < 100) 0 else (power / 100) % 10
        return h - 5
    }
}