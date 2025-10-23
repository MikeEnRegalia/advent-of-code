package ec2024

fun main() {
    val grids = generateSequence(::readLine).toList()

    val runicWords = mutableListOf<String>()
    for (offsetY in grids.indices.filter { it % 9 == 0 }) {
        for (offsetX in grids[offsetY].indices.filter { it % 9 == 0 }) {
            val grid = grids
                .filterIndexed { i, _ -> i in (offsetY..<offsetY + 8) }
                .map { it.filterIndexed { j, _ -> j in (offsetX..<offsetX + 8) } }

            val runicWord = buildString {
                for (y in 2..5) {
                    for (x in 2..5) {
                        val runes = (grid.map { it[x] } + grid[y].toList()).filter { it !in " ." }
                        append(runes.maxBy { rune -> runes.count { it == rune } })
                    }
                }
            }
            runicWords += runicWord
        }
    }

    fun Char.basePower() = code - 'A'.code + 1
    fun String.power() = indices.sumOf { (it + 1) * this[it].basePower() }

    println(runicWords.sumOf { it.power() })
}
