package y2018.day11

fun day11(serial: Int): Pair<Int, Int> {
    val grid = sequence {
        for (x in 1..300)
            for (y in 1..300) yield(Cell(x, y))
    }.toList()

    return sequence {
        for (x in 1..298)
            for (y in 1..298) {
                yield(grid.filter { it.x in x..x + 2 && it.y in y..y + 2 })
            }
    }.maxByOrNull { square -> square.sumOf { it.power(serial) } }!!
        .also { println(it) }
        .let { it[0].x to it[0].y }
}


internal data class Cell(val x: Int, val y: Int) {
    fun power(serial: Int): Int {
        val rackId = x + 10
        val power = (rackId * y + serial) * rackId
        val h = if (power < 100) 0 else (power / 100) % 10
        return h - 5
    }
}