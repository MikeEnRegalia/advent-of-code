package y2018.day01

fun main(): Unit = with(loadInput()) {
    println("part 1: ${sum()}, part 2: ${firstDuplicateSum()})")
}

private fun loadInput() = generateSequence(::readLine).map { it.toInt() }.toList()

private fun List<Int>.firstDuplicateSum(): Long {
    val frequencies = mutableSetOf<Long>(0)
    var sum = 0L
    var pos = 0
    while (true) {
        sum += this[pos++ % size]
        if (!frequencies.add(sum)) return sum
    }
}