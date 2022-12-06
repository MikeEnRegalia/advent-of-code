package aoc2022

fun main() = solveWithSequence()

fun solveImperatively() {
    with(String(System.`in`.readAllBytes())) {
        fun String.afterMarker(i: Int, n: Int) =
            i > n && with(mutableSetOf<Char>()) { (i - n until i).all { add(get(it)) } }

        var part1 = false
        indices.forEach { i ->
            if (!part1 && afterMarker(i, 4)) {
                println(i)
                part1 = true
            }
            if (afterMarker(i, 14)) {
                println(i)
                return
            }
        }
    }
}

private fun solveWithSequence() {
    var part1 = false
    val marker = mutableSetOf<Char>()
    System.`in`.run { generateSequence { read().takeUnless { it == -1 }?.toChar() } }.forEachIndexed { i, c ->
        if (marker.size == 4 && !part1) {
            println(i)
            part1 = true
        } else if (marker.size == 14) {
            println(i)
            return
        }
        if (!marker.add(c)) marker.clear()
    }
}

private fun day06(s: String) = listOf(4, 14)
    .map { n -> s.windowedSequence(n, transform = CharSequence::toSet).indexOfFirst { it.size == n } + n }
