package aoc2021b

import kotlin.math.max
import kotlin.math.min

val matrix = generateSequence(::readLine).map { it.map(Char::digitToInt).toMutableList() }.toMutableList()

var flashes = 0
var steps = 0
while (true) {
    steps++
    matrix.forEach { for (x in it.indices) it[x] = it[x] + 1 }
    while (true) {
        val flashing = matrix.flatMapIndexed { y, r -> r.mapIndexedNotNull { x, b -> if (b > 9) x to y else null } }
            .takeUnless(List<*>::isEmpty) ?: break
        for ((x, y) in flashing) {
            for (dx in max(0, x - 1)..min(9, x + 1))
                for (dy in max(0, y - 1)..min(9, y + 1))
                    if (matrix[dy][dx] != -1) matrix[dy][dx] = matrix[dy][dx].inc()
            matrix[y][x] = -1
            if (steps <= 100) flashes++
        }
    }
    matrix.forEach { for (x in it.indices) it[x] = max(0, it[x]) }
    if (matrix.sumOf { row -> row.sumOf { it } } == 0) break
}

println(flashes)
println(steps)