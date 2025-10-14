package aoc2024

fun main() {
    val links = generateSequence(::readlnOrNull).map { it.split("-").toSet() }.toSet()
    fun Set<String>.areAllConnectedTo(other: String) = all { setOf(it, other) in links }

    val nodes = links.flatten().toSet()

    links.filter { link -> link.any { it.startsWith("t") } }
        .flatMap { link -> nodes.filter { link.areAllConnectedTo(it) }.map { link + it } }
        .distinct().count()
        .also(::println)

    nodes.map { mutableSetOf(it) }
        .onEach { it.addAll(generateSequence { nodes.firstOrNull(it::areAllConnectedTo) }) }
        .maxBy { it.size }
        .sorted().joinToString(",")
        .also(::println)
}
