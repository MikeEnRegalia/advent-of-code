package aoc2024

fun main() {
    val connections = generateSequence { readlnOrNull() }
        .map { it.split("-").toSet() }
        .toSet()

    val computers = connections.flatten().toSet()

    val parties = connections.flatMap { connection ->
        computers.mapNotNull { third ->
            when {
                third in connection -> null
                connection.all { setOf(it, third) in connections } -> connection + third
                else -> null
            }
        }
            .toSet()
    }.toSet()

    println(parties.count { party -> party.any { it.startsWith("t") } })

    val bigParties = mutableSetOf<Set<String>>()

    for (computer in computers) {
        if (bigParties.any { computer in it }) continue

        val party = buildSet {
            add(computer)
            do {
                computers
                    .filter { it !in this }
                    .firstOrNull { node -> all { setOf(it, node) in connections } }
                    ?.let { add(it) } ?: break

            } while (true)
        }
        bigParties.add(party)
    }

    println(bigParties.maxBy { it.size }.sorted().joinToString(","))
}
