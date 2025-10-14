package aoc2024

fun main() {
    val links = generateSequence { readlnOrNull()?.split("-")?.toSet() }.toSet()

    val computers = links.flatten().toSet()

    val tLinks = links.filter { link -> link.any { it.startsWith("t") } }

    val parties = buildSet {
        for (link in tLinks) for (c in computers) {
            if (link.all { setOf(it, c) in links })
                add(link + c)
        }
    }

    println(parties.count())

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
