package aoc2024

fun main() {
    val links = generateSequence { readlnOrNull()?.split("-")?.toSet() }.toSet()

    val nodes = links.flatten().toSet()

    links.asSequence()
        .filter { it.any { n -> n.startsWith("t") } }
        .flatMap { link -> nodes.filter { c -> link.all { n -> setOf(n, c) in links } }.map { link + it } }
        .toSet()
        .count()
        .also(::println)

    val bigParties = mutableSetOf<Set<String>>()

    for (computer in nodes) {
        if (bigParties.any { computer in it }) continue
        bigParties.add(buildSet {
            add(computer)
            do {
                nodes.firstOrNull { node -> all { setOf(it, node) in links } }?.let { add(it) } ?: break
            } while (true)
        })
    }

    println(bigParties.maxBy { it.size }.sorted().joinToString(","))
}
