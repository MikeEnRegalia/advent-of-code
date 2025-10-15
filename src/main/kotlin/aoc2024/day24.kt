package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()

    val init = lines.filter { ':' in it }.associate { it.split(": ").let { it[0] to (it[1].toInt() == 1) } }
    val gates = lines.filter { "->" in it }.map { it.split(" ") }

    val Z = gates.map { it.last() }.filter { it.startsWith("z") }.sortedDescending()

    val wires = init.toMutableMap()

    while (Z.any { it !in wires }) {
        for ((aWire, op, bWire, _, resultWire) in gates) {
            val a = wires[aWire] ?: continue
            val b = wires[bWire] ?: continue

            if (resultWire !in wires) wires[resultWire] = when (op) {
                "AND" -> a && b
                "OR" -> a || b
                else -> a xor b
            }
        }
    }

    println(Z.joinToString("") { if (wires[it] == true) "1" else "0" }.toLong(2))

    fun String.leadsTo(vararg operations: String) = gates.mapNotNull { (a, op, b) ->
        if (a == this || b == this) op else null
    }.toSet().let { operations.all { op -> op in it }}

    val bogus = gates.filterNot { (a, op, b, _, wire) ->
        when (op) {
            "OR" -> wire == Z.first() || wire.leadsTo("XOR", "AND")
            "AND" -> when {
                setOf(a, b) == setOf("x00", "y00") -> wire.leadsTo("XOR", "AND")
                else -> wire.leadsTo("OR")
            }

            "XOR" -> when {
                wire == "z00" -> setOf(a, b) == setOf("x00", "y00")
                setOf(a, b).any { it.startsWith("x") } -> wire.leadsTo("XOR", "AND")
                else -> wire in Z
            }

            else -> true
        }
    }

    println(bogus.map { it.last() }.sorted().joinToString(","))
}
