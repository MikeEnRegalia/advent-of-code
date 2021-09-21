package day10

fun main() {
    "1321131112"
        .map { it.toString().toInt() }
        .murf(40)
        .also { println("murf: ${it.size}") }
        .murf(10)
        .also { println("murf more: ${it.size}") }
}

fun List<Int>.murf(times: Int = 1): List<Int> =
    murfOnce().let { if (times == 1) it else it.murf(times - 1) }

private fun List<Int>.murfOnce(): MutableList<Int> {
    val result = mutableListOf<Int>()
    var index = 0
    while (index < size) {
        segment(index).let { (len, n) ->
            result += len
            result += n
            index += len
        }
    }
    return result
}

private fun List<Int>.segment(index: Int): Pair<Int, Int> {
    val n = this[index]
    var to = index
    while (to < size && this[to] == n) ++to
    return Pair(to - index, n)
}