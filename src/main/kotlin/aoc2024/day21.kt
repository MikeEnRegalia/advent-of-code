package aoc2024

import kotlin.math.min

fun main() {
    val codes = generateSequence(::readLine).toList()

    data class Point(val x: Int, val y: Int)

    data class State(
        val numbers: String = "",
        val pad: Point = Point(2, 0),
        val pad2: Point = Point(2, 0),
        val numPad: Point = Point(2, 3)
    )

    fun toGrid(input: List<String>) = input.flatMapIndexed { y, s ->
        s.mapIndexedNotNull { x, c ->
            if (c != ' ') Point(x, y) to c else null
        }
    }.toMap()

    val directionalPad = toGrid(listOf(" ^A", "<v>"))
    val numericPad = toGrid(listOf("789", "456", "123", " 0A"))

    fun solve() {
        fun State.step(): List<State> {
            return listOfNotNull(
                copy(pad = pad.copy(y = pad.y - 1)),
                copy(pad = pad.copy(y = pad.y + 1)),
                copy(pad = pad.copy(x = pad.x - 1)),
                copy(pad = pad.copy(x = pad.x + 1)),
                when (directionalPad.getValue(pad)) {
                    '<' -> copy(pad2 = pad2.copy(x = pad2.x - 1))
                    '>' -> copy(pad2 = pad2.copy(x = pad2.x + 1))
                    '^' -> copy(pad2 = pad2.copy(y = pad2.y - 1))
                    'v' -> copy(pad2 = pad2.copy(y = pad2.y + 1))
                    else -> when (directionalPad.getValue(pad2)) {
                        '<' -> copy(numPad = numPad.copy(x = numPad.x - 1))
                        '>' -> copy(numPad = numPad.copy(x = numPad.x + 1))
                        '^' -> copy(numPad = numPad.copy(y = numPad.y - 1))
                        'v' -> copy(numPad = numPad.copy(y = numPad.y + 1))
                        else -> copy(numbers = numbers + numericPad.getValue(numPad))
                    }
                }
            )
                .filter { it.pad in directionalPad }
                .filter { it.pad2 in directionalPad }
                .filter { it.numPad in numericPad }
                .filter { codes.any { it.startsWith(numbers) } && codes.none { it.length < numbers.length } }
        }

        val start = State()
        val V = mutableSetOf<State>()
        val D = mutableMapOf(start to 0)
        val U = mutableSetOf(start)

        val codeSequences = mutableMapOf<String, Int>()

        while (U.isNotEmpty()) {
            val curr = U.minBy { D.getValue(it) }

            for (next in curr.step().filter { it !in V }) {
                U += next
                val (prevCost, cost) = (D[next] ?: Int.MAX_VALUE) to D.getValue(curr) + 1
                if (cost < prevCost) D[next] = cost
            }

            V += curr
            U -= curr

            codeSequences.compute(curr.numbers) { key, old -> min(old ?: Int.MAX_VALUE, D.getValue(curr)) }

            if (codes.all { it in codeSequences.keys }) {
                break
            }
        }

        println(codeSequences.filterKeys { it in codes })
        println(codeSequences.filterKeys { it in codes }.entries.sumOf {
            it.key.filter { it.isDigit() }.toLong() * it.value
        })
    }

    solve()
}
