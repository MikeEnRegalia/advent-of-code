package aoc2021b

fun main() {
    with(generateSequence(::readLine).map { it.toInt() }.toList()) {
        println(countInc(2))
        println(countInc(4))
    }
}

private fun List<Int>.countInc(d: Int) =
    windowed(d).count { it.first() < it.last() }
