package aoc2024

import kotlin.math.min

fun main() {
    val machines = generateSequence(::readLine)
        .filterNot { it.isBlank() }
        .map { it.split(" ").map { it.filter { it.isDigit() } }.filter { it.isNotBlank() }.map(String::toInt) }
        .chunked(3).toList()

    fun solve(add: Long = 0L) = machines.sumOf { (a, b, p) ->
        val (ax, ay) = a
        val (bx, by) = b
        val (_px, _py) = p
        val (px, py) = _px + add to _py + add

        val maxA = min((px / ax), (py / ay))
        val maxB = min((px / bx), (py / by))

        val pushes = (0..maxA).flatMap { fa ->
            (0..maxB).asSequence()
                .filter { fb -> fa * ax + fb * bx == px && fa * ay + fb * by == py }
                .map { fb -> fa to fb }
        }

        val cost = pushes.minOfOrNull { (pa, pb) -> pa * 3L + pb }

        println("$maxA x $a $maxB x $b = $p: $pushes -> $cost")

        cost ?: 0L
    }

    println(solve())
    println(solve(10_000_000_000_000L))
}
