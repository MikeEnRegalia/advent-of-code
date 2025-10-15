package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()

    val init = lines.filter { ':' in it }.associate { it.split(": ").let { it[0] to (it[1].toInt() == 1) } }
    val gates = lines.filter { "->" in it }.map { it.split(" ") }

    val targetWires = gates.map { it.last() }.filter { it.startsWith("z") }.sortedDescending()

    val wires = init.toMutableMap()

    while (targetWires.any { it !in wires }) {
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

    println(targetWires.joinToString("") { if (wires[it] == true) "1" else "0" }.toLong(2))

    val lastWire = "z${targetWires.lastIndex}"

    fun String.connectedOperations() = gates.mapNotNull { (a, op, b) ->
        if (listOf(a, b).any { it == this }) op else null
    }

    val bogus = gates.filterNot { (_, op, _, _, wire) ->
        when (op) {
            "OR" -> wire == lastWire || "XOR" in wire.connectedOperations()
            "AND" -> "OR" in wire.connectedOperations()
            "XOR" -> wire in targetWires || "XOR" in wire.connectedOperations()
            else -> true
        }
    }.onEach(::println)

    // not dgr,dtv,mtj,njb,vvm,z12,z29,z37
    println(bogus.map { it.last() }.sorted().joinToString(","))
}
