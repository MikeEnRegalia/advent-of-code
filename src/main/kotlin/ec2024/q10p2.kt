package ec2024

fun main() {
    val grids = generateSequence(::readLine).toList()

    fun List<String>.subGrid(x: Int, y: Int, size: Int) =
        filterIndexed { i, _ -> i in (y..<y + size) }
            .map { it.filterIndexed { i, _ -> i in (x..<x + size) } }

    fun getRunicWord(grid: List<String>) = buildString {
        for (y in 2..5) for (x in 2..5) {
            val runes = (grid.map { it[x] } + grid[y].toList()).filter { it != '.' }
            append(runes.maxBy { rune -> runes.count { it == rune } })
        }
    }

    val runicWords = grids.indices.filter { it % 9 == 0 }.flatMap { offsetY ->
        grids[offsetY].indices.filter { it % 9 == 0 }.map { offsetX ->
            getRunicWord(grids.subGrid(offsetX, offsetY, 8))
        }
    }

    fun Char.basePower() = code - 'A'.code + 1
    fun String.power() = indices.sumOf { (it + 1) * this[it].basePower() }

    println(runicWords.sumOf { it.power() })
}
