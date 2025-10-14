package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()

    val initialWires = lines.filter { ':' in it }.associate { it.split(": ").let { it[0] to (it[1].toInt() == 1) } }
    val rules = lines.filter { "->" in it }.map { it.split(" ") }

    val targets = rules.map { it.last() }.filter { it.startsWith("z") }.sortedDescending()

    val wires = initialWires.toMutableMap()

    while (targets.any { it !in wires }) {
        for ((aWire, op, bWire, _, resultWire) in rules) {
            val a = wires[aWire] ?: continue
            val b = wires[bWire] ?: continue

            if (resultWire !in wires) wires[resultWire] = when (op) {
                "AND" -> a && b
                "OR" -> a || b
                else -> a xor b
            }
        }
    }

    println(targets.joinToString("") { if (wires[it] == true) "1" else "0" }.toLong(2))
}
