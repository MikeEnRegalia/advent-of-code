package y2018.day11

fun day11(serial: Int, anySquare: Boolean = false) = sequence {
    var max: Int? = null
    for (size in if (anySquare) 1..300 else 3..3) {
        println(size)
        for (topLeftX in 1..300 - (size - 1))
            for (topLeftY in 1..300 - (size - 1)) {
                val power = square(topLeftX, topLeftY, size, serial)
                if (max != null && power <= max) continue
                max = power
                println("$size: $power")
                yield(Square(topLeftX, topLeftY, size, power))
            }
    }
}.maxByOrNull { it.power }!!.let { listOf(it.x, it.y, it.size) }

fun square(topLeftX: Int, topLeftY: Int, size: Int, serial: Int): Int {
    var sum = 0
    for (x in 0 until size) {
        for (y in 0 until size) sum += power(topLeftX + x, topLeftY + y, serial)
    }
    return sum
}

internal class Square(val x: Int, val y: Int, val size: Int, val power: Int)

fun power(x: Int, y: Int, serial: Int): Int {
    val rackId = x + 10
    val power = (rackId * y + serial) * rackId
    val h = if (power < 100) 0 else (power / 100) % 10
    return h - 5
}
