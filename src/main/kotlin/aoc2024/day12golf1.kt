package aoc2024

typealias Plot = Pair<Int, Int>

fun main() {
    val grid = generateSequence(::readLine).flatMapIndexed { y, l -> l.mapIndexed { x, c -> x to y to c } }.toMap()

    fun Plot.neighbors() =
        sequenceOf(first - 1 to second, first + 1 to second, first to second - 1, first to second + 1)
            .filter { grid[it] == grid[this] }

    println(
        grid
            .keys
            .fold(mutableSetOf<Pair<Char, Set<Plot>>>()) { acc, plot ->
                if (acc.none { plot in it.second })
                    acc += grid[plot]!! to buildSet {
                        add(plot)
                        while (true) flatMap(Plot::neighbors).filterNot(::contains).takeIf { it.isNotEmpty() }
                            ?.also(::addAll)
                            ?: break
                    }
                acc
            }.sumOf { it.second.size * it.second.sumOf { 4 - it.neighbors().count() } }
    )
}
