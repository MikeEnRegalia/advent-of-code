package aoc2021b

fun Map<String, Set<String>>.explore(paths: List<String> = listOf("start"), twiceAllowed: Boolean): List<List<String>> {
    val here = paths.last().takeUnless { it == "end" } ?: return listOf(paths)
    val canVisitSmallTwice = twiceAllowed && paths.filter { it == it.lowercase() }.let { it == it.distinct() }
    return getValue(here)
        .filter { if (canVisitSmallTwice) it != "start" else it == it.uppercase() || !paths.contains(it) }
        .flatMap { explore(paths.plus(it), twiceAllowed) }
}

buildMap<String, Set<String>> {
    for (line in generateSequence(::readLine)) line.split("-").let { (a, b) ->
        compute(a) { _, v -> v?.plus(b) ?: setOf(b) }
        compute(b) { _, v -> v?.plus(a) ?: setOf(a) }
    }
}.let { caves -> listOf(false, true).map { caves.explore(twiceAllowed = it).size }.forEach(::println) }