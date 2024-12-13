package aoc2024

import kotlin.math.min

fun main() {
    val machines = generateSequence(::readLine)
        .filterNot { it.isBlank() }
        .map { it.split(" ").map { it.filter { it.isDigit() } }.filter { it.isNotBlank() }.map(String::toInt) }
        .chunked(3).toList()

    fun gcd(numbers: List<Long>): Long {
        require(numbers.isNotEmpty()) { "List must not be empty" }
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            var num1 = result
            var num2 = numbers[i]
            while (num2 != 0L) {
                val temp = num2
                num2 = num1 % num2
                num1 = temp
            }
            result = num1
        }
        return result
    }

    fun solve(add: Long = 0L) = machines.sumOf { (a, b, p) ->
        val (ax, ay) = a
        val (bx, by) = b
        val (__px, __py) = p
        val (px, py) = __px + add to __py + add


        val maxA = min((px / ax), (py / ay))
        val maxB = min((px / bx), (py / by))

        val push = (maxA downTo 0).firstNotNullOfOrNull { fa ->
            val rx = px - fa * ax
            val ry = py - fa * ay
            if (rx % bx != 0L || ry % by != 0L || rx / bx != ry / by) null else fa to rx / bx
        }

        val cost = push?.let { (fa, fb) ->fa * 3L + fb }

        println("$maxA x $a $maxB x $b = $p: $push -> $cost")

        cost ?: 0L
    }

    println(solve())
    //println(solve(10_000_000_000_000L))
}
