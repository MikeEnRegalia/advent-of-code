package aoc2018.day01

fun main() = with(loadInput()) {
    println("part 1: ${sum()}, part 2: ${part2()}")
}

private fun loadInput() =
    generateSequence(::readLine).map { it.toInt() }.toList()

private fun List<Int>.part2(): Int {
    val history = mutableSetOf(0)
    var sum = 0
    circularForEach {
        sum += it
        if (!history.add(sum)) return sum
    }
}

private inline fun List<Int>.circularForEach(action: (Int) -> Unit): Nothing {
    var pos = 0
    while (true) {
        action(get(pos++ % size))
    }
}