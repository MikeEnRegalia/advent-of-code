package aoc2024

fun main() {
    val links = generateSequence { readlnOrNull()?.split("-")?.toSet() }.toSet()
    val nodes = links.flatten().toSet()

    links
        .filter { it.any { n -> n.startsWith("t") } }
        .flatMap { link -> nodes.filter { c -> link.all { n -> setOf(n, c) in links } }.map { link + it } }
        .distinct()
        .count()
        .also(::println)

    val bigParties = nodes.map {
        mutableSetOf(it).apply {
            generateSequence { nodes.firstOrNull { new -> all { n -> setOf(n, new) in links } } }
                .forEach(::add)
        }
    }

    println(bigParties.maxBy { it.size }.sorted().joinToString(","))
}
