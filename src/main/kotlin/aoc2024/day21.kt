package aoc2024

import kotlin.math.min

fun main() {
    val codes = generateSequence(::readLine).toList()

    data class Point(val x: Int, val y: Int)

    fun toGrid(input: List<String>) = input.flatMapIndexed { y, s ->
        s.mapIndexedNotNull { x, c -> if (c != ' ') Point(x, y) to c else null }
    }.toMap()

    val DPAD = toGrid(listOf(" ^A", "<v>"))
    val NPAD = toGrid(listOf("789", "456", "123", " 0A"))

    data class State(
        val numbers: String = "",
        val pads: List<Point>,
        val numPad: Point = Point(2, 3)
    ) {
        override fun toString() = "${pads.map { DPAD[it] }} ${NPAD[numPad]}: \"${numbers}\""
    }

    fun State.nextOnNumericPad(c: Char) = with(numPad) {
        when (c) {
            '<' -> copy(x = x - 1)
            '>' -> copy(x = x + 1)
            '^' -> copy(y = y - 1)
            'v' -> copy(y = y + 1)
            else -> null
        }
    }?.let { copy(numPad = it) } ?: copy(numbers = numbers + NPAD.getValue(numPad))

    fun State.pressA(): State {
        for ((i, p) in pads.withIndex()) {
            if (i == 0) continue
            when (DPAD[pads[i - 1]]) {
                '<' -> return copy(pads = pads.mapIndexed { j, it -> if (i == j) p.copy(x = p.x - 1) else it })
                '>' -> return copy(pads = pads.mapIndexed { j, it -> if (i == j) p.copy(x = p.x + 1) else it })
                '^' -> return copy(pads = pads.mapIndexed { j, it -> if (i == j) p.copy(y = p.y - 1) else it })
                'v' -> return copy(pads = pads.mapIndexed { j, it -> if (i == j) p.copy(y = p.y + 1) else it })
            }
        }
        return nextOnNumericPad(DPAD.getValue(pads.last()))
    }

    fun solve(nPads: Int): Long {
        fun State.pressButtons(): List<State> =
            with(pads[0]) { listOf(copy(y = y - 1), copy(y = y + 1), copy(x = x + 1), copy(x = x - 1)) }
                .map { copy(pads = listOf(it) + pads.drop(1)) }
                .let { it + pressA() }

        val start = State(pads = MutableList(nPads) { Point(2, 0) })
        val V = mutableSetOf<State>()
        val D = mutableMapOf(start to 0)
        val U = mutableSetOf(start)

        val codeSequences = mutableMapOf<String, Int>()

        while (U.isNotEmpty()) {
            val curr = U.minBy { D.getValue(it) }

            for (next in curr.pressButtons()
                .filter { it.pads.all(DPAD::contains) && NPAD.contains(it.numPad) }
                .filter { s -> codes.any { it.startsWith(s.numbers) } && codes.none { it.length < s.numbers.length } }
                .filter { it !in V }) {

                U += next
                val (prevCost, cost) = (D[next] ?: Int.MAX_VALUE) to D.getValue(curr) + 1
                if (cost < prevCost) D[next] = cost
            }

            V += curr
            U -= curr

            codeSequences.compute(curr.numbers) { _, old -> min(old ?: Int.MAX_VALUE, D.getValue(curr)) }

            if (codes.all { it in codeSequences.keys }) {
                break
            }
        }

        return codeSequences.filterKeys { it in codes }.entries.sumOf {
            it.key.filter(Char::isDigit).toLong() * it.value
        }
    }

    var prev = 0L
    repeat(25) {
        val n = solve(it+1)
        println(n - prev)
        prev = n
    }
}
