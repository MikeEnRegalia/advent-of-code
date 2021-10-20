package aoc2018.day22

fun day22(depth: Int, targetX: Int, targetY: Int): Int = with(Problem(depth, targetX, targetY)) {
    (0..targetX)
        .flatMap { x -> (0..targetY).map { y -> erosionLevel(x, y) % 3 } }
        .sum()
}

data class Problem(val depth: Int, val targetX: Int, val targetY: Int) {
    val erosionLevels: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    fun geologicalIndex(x: Int, y: Int): Int = when {
        x == 0 && y == 0 || x == targetX && y == targetY -> 0
        x == 0 -> y * 48271
        y == 0 -> x * 16807
        else -> erosionLevel(x - 1, y) * erosionLevel(x, y - 1)
    }

    fun erosionLevel(x: Int, y: Int) =
        erosionLevels[x to y] ?: ((geologicalIndex(x, y) + depth) % 20183).also { erosionLevels[x to y] = it }
}
