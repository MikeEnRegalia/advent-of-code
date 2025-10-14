package aoc2024

fun main() {
    val links = generateSequence { readlnOrNull()?.split("-")?.toSet() }.toSet()

    val computers = links.flatten().toSet()

    val parties = links.flatMap { connection ->
        computers.mapNotNull { third ->
            when {
                third in connection -> null
                connection.all { setOf(it, third) in links } -> connection + third
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
                    .firstOrNull { node -> all { setOf(it, node) in links } }
                    ?.let { add(it) } ?: break

            } while (true)
        }
        bigParties.add(party)
    }

    println(bigParties.maxBy { it.size }.sorted().joinToString(","))
}
