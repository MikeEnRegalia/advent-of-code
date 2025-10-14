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
}
