package aoc2021b

fun main() {
    AoCDay11.solve(generateSequence(::readLine).toList()).forEach(::println)
}

object AoCDay11 {
    data class Pos(val x: Int, val y: Int) {
        fun neighbors() = sequence {
            for (x in -1..1)
                for (y in -1..1)
                    if (x != 0 || y != 0)
                        yield(copy(x = this@Pos.x + x, y = this@Pos.y + y))
        }
    }

    fun solve(lines: List<String>): List<Any?> {
        val cave = lines.flatMapIndexed { y, row -> row.mapIndexed { x, c -> Pos(x, y) to c.digitToInt() } }
            .toMap().toMutableMap()

        var part1 = 0
        var step = 0
        while (true) {
            ++step
            cave.replaceAll { _, u -> u + 1 }
            val flashed = mutableSetOf<Pos>()
            fun Pos.flash() {
                flashed += this
                cave[this] = 0
                neighbors()
                    .filterNot { it in flashed }
                    .forEach { cave.computeIfPresent(it) { _, energy -> energy + 1 } }
            }

            while (true) cave.filter { it.value > 9 }.firstNotNullOfOrNull { it.key }?.flash() ?: break

            if (step <= 100) part1 += flashed.size
            if (flashed.size == cave.size) break
        }

        return listOf(part1 to step)
    }
}