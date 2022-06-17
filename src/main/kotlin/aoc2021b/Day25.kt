package aoc2021b

fun main() {
    Day25(generateSequence(::readLine).toList()).solve().also { println(it) }
}

class Day25(private val input: List<String>) {
    data class Pos(val x: Int, val y: Int)

    fun solve(): Any {
        val map =
            input.flatMapIndexed { y, l -> l.mapIndexedNotNull { x, c -> if (c == '.') null else Pos(x, y) to c } }
                .toMap()
        val width = map.keys.maxOf { it.x } + 1
        val height = map.keys.maxOf { it.y } + 1
        var transformed = map
        var step = 0
        while (true) {
            step++

            var new = moveEast(transformed, width)
            new = moveSouth(new, height)

            if (new.keys == transformed.keys) return step
            transformed = new
        }
    }

    private fun moveEast(transformed: Map<Pos, Char>, width: Int) = buildMap {
        transformed.forEach { (pos, c) ->
            val next = when (c) {
                '>' -> Pos((pos.x + 1) % width, pos.y).takeIf { it !in transformed } ?: pos
                'v' -> pos
                else -> throw IllegalArgumentException()
            }
            put(next, c)
        }
    }

    private fun moveSouth(transformed: Map<Pos, Char>, height: Int) = buildMap {
        transformed.forEach { (pos, c) ->
            val next = when (c) {
                'v' -> Pos(pos.x, (pos.y + 1) % height).takeIf { it !in transformed } ?: pos
                '>' -> pos
                else -> throw IllegalArgumentException()
            }
            put(next, c)
        }
    }

}