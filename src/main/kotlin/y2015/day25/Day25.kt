package y2015.day25

data class Pos(val row: Int, val col: Int, val code: Long = 20151125L) {
    fun next() = Pos(
        if (row == 1) col + 1 else row - 1,
        if (row == 1) 1 else col + 1,
        (code * 252533) % 33554393
    )
}

fun main(vararg args: String) {
    val row = args[0].toInt()
    val col = args[1].toInt()

    sequence {
        var pos = Pos(1, 1)
        while (true) {
            yield(pos)
            pos = pos.next()
        }
    }.first {
        it.row == row && it.col == col
    }.also { println(it.code) }
}

