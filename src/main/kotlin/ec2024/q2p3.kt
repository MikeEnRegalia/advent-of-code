package ec2024

fun main() {
    val data = generateSequence(::readLine).toList()
    val words = data.first().substringAfter(":").split(",")
    val grid = data.drop(2)

    val scales = mutableSetOf<Pair<Int, Int>>()

    fun Pair<Int, Int>.scale() = when {
        second !in grid.indices || first !in grid[second].indices -> null
        else -> grid[second][first]
    }

    val rowLength = grid.first().length

    for (x in grid.first().indices) {
        for (y in grid.indices) {
            for (word in words) {
                for (positions in sequenceOf(
                    (x - word.lastIndex..x).reversed().map { x -> Pair(x, y) },
                    (x..x + word.lastIndex).map { x -> Pair(x, y) },
                    (y - word.lastIndex..y).reversed().map { y -> Pair(x, y) },
                    (y..y + word.lastIndex).map { y -> Pair(x, y) }
                ).map { it.map { (x, y) -> (x + rowLength) % rowLength to y } }) {
                    val actualWord = positions.map { it.scale() }.joinToString("") { it?.toString() ?: " " }
                    if (actualWord == word) scales += positions
                }
            }
        }
    }

    println(scales.size)
}
