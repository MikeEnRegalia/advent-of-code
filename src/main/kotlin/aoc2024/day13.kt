package aoc2024

import kotlin.math.min

fun main() {
    val machines = generateSequence(::readLine)
        .filterNot { it.isBlank() }
        .map { it.split(" ").map { it.filter { it.isDigit() } }.filter { it.isNotBlank() }.map(String::toInt) }
        .chunked(3).toList()

    val part1 = machines.sumOf { (a, b, p) ->
        val (ax, ay) = a
        val (bx, by) = b
        val (px, py) = p

        val maxA = min((px / ax) + 1000, (py / ay) + 1000)
        val maxB = min((px / bx) + 1000, (py / by) + 1000)

        val pushes = (0..maxA).flatMap { fa ->
            (0..maxB).asSequence()
                .filter { fb -> fa * ax + fb * bx == px && fa * ay + fb * by == py }
                .map { fb -> fa to fb }
        }

        val cost = pushes.minOfOrNull { (pa, pb) -> pa * 3 + pb }

        println("$maxA x $a $maxB x $b = $p: $pushes -> $cost")

        cost ?: 0
    }

    println(part1)
}
