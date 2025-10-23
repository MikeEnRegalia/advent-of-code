package ec2024

fun main() {
    val grid = generateSequence(::readLine).toList()

    val runicWord = buildString {
        for (y in 2..5) {
            for (x in 2..5) {
                val runes = (grid.map { it[x] } + grid[y].toList()).filter { it != '.' }
                println(runes)
                append(runes.maxBy { rune -> runes.count { it == rune }})
            }
        }
    }

    println(runicWord)
}
