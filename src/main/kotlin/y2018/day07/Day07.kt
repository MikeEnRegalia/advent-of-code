package y2018.day07

fun main() {
    val rules = generateSequence(::readLine)
        .map {
            """^Step (\w) must be finished before step (\w) can begin.$""".toRegex()
                .find(it)!!.destructured.let { (before, step) -> before to step }
        }
        .toList()

    val steps = with(rules) { sequenceOf(map { it.first }, map { it.second }) }.flatten().distinct()

    val done = mutableListOf<String>()
    while (true) {
        steps
            .filterNot { done.contains(it) }
            .filterNot { rules.filterNot { done.contains(it.first) }.map { it.second }.contains(it) }
            .sorted()
            .firstOrNull()
            ?.let { done.add(it) } ?: break
    }

    done.joinToString("").also { println(it) }
}

