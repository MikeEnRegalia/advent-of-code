package aoc2024

import kotlin.math.min

fun main() {
    val machines = generateSequence(::readLine)
        .filterNot { it.isBlank() }
        .map { it.split(" ").map { it.filter { it.isDigit() } }.filter { it.isNotBlank() }.map(String::toInt) }
        .chunked(3).toList()

    fun solve(add: Long = 0L) = machines.parallelStream().map { (a, b, p) ->
        val (ax, ay) = a
        val (bx, by) = b
        val (__px, __py) = p
        val (px, py) = __px + add to __py + add

        val maxA = min((px / ax), (py / ay))

        var push: Pair<Long, Long>? = null
        for (fa in 0..maxA) {
            val rx = px - fa * ax
            val ry = py - fa * ay
            if (rx % bx != 0L || ry % by != 0L || rx/bx != ry/by)  continue

            val fb = rx/bx

            val tx = fa * ax + fb * bx
            val ty = fa * ay + fb * by
            val fx = px / tx
            val fy = py / ty

            if (px % tx == 0L && py % ty == 0L && fx == fy) {
                push = fa * fx to fb * fx
                break
            }
        }

        val cost = push?.let { (fa, fb) -> fa * 3L + fb }

        println("$a x $b = $p: $cost $push")

        cost ?: 0L
    }.reduce(Long::plus)

    println(solve())
    println(solve(10_000_000_000_000L))
}
