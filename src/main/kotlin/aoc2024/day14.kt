package aoc2024

fun main() {
    val (width, height) = 101 to 103

    data class Robot(val x: Int, val y: Int, val vx: Int, val vy: Int)

    fun Robot.move(dx: Int, dy: Int): Robot {
        val newX = (x + dx).let {
            if (it < 0) it + width else if (it >= width) it - width else it
        }
        val newY = (y + dy).let {
            if (it < 0) it + height else if (it >= height) it - height else it
        }
        return copy(x = newX, y = newY)
    }

    val robots = generateSequence(::readLine)
        .map { it.split(" ").map { it.drop(2).split(",").map { it.toInt() } } }
        .map { (p, v) -> Robot(p[0], p[1], v[0], v[1]) }
        .toList()

    fun part1(): Int {
        var moving = robots.toMutableList()
        repeat(10000) { i ->
            moving = moving.map {
                it.move(it.vx, it.vy)
            }.toMutableList()
            if ((i-13) % 101 == 0) {
                println(i+1)
                for (y in 0..<height) {
                    for (x in 0..<width) {
                        print(if (moving.any { it.x == x && it.y == y }) "X" else " ")
                    }
                    println()
                }
            }
        }


        val halfWidth = width / 2
        val halfHeight = height / 2
        return sequenceOf(
            listOf(0..<halfWidth, 0..<halfHeight),
            listOf(halfWidth + 1..<width, 0..<halfHeight),
            listOf(0..<halfWidth, halfHeight + 1..<height),
            listOf(halfWidth + 1..<width, halfHeight + 1..<height),
        ).map { (xrange, yrange) -> moving.filter { it.x in xrange && it.y in yrange }.size.also(::println) }
            .reduce(Int::times)
    }

    println(part1())
}
