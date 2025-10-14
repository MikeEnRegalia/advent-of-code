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

    val bigParties = buildSet {
        for (computer in nodes) add(buildSet {
            add(computer)
            generateSequence { nodes.firstOrNull { node -> all { setOf(it, node) in links } } }
                .forEach(::add)
        })
    }

    println(bigParties.maxBy { it.size }.sorted().joinToString(","))
}
