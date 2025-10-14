package aoc2024

fun main() {
    val links = generateSequence(::readlnOrNull).map { it.split("-").toSet() }.toSet()
    fun Set<String>.areAllConnectedTo(other: String) = all { setOf(it, other) in links }

    val computers = links.flatten().toSet()

    links.filter { link -> link.any { it.startsWith("t") } }
        .flatMap { link -> computers.filter { link.areAllConnectedTo(it) }.map { link + it } }
        .distinct().count()
        .also(::println)

    val parties = computers.map { mutableSetOf(it) }.onEach {
        it.addAll(generateSequence { computers.firstOrNull(it::areAllConnectedTo) })
    }

    println(parties.maxBy { it.size }.sorted().joinToString(","))
}
