package aoc2024

fun main() {
    fun List<String>.parse(sep: Char) = filter { sep in it }.map { it.split(sep).map(String::toInt) }
    val lines = generateSequence(::readLine).toList()
    val (rules, updates) = listOf('|', ',').map { lines.parse(it) }

    fun List<List<Int>>.checksum() = sumOf { it[it.size / 2] }

    val correctlyOrdered = updates.filter { update ->
        rules.filter(update::containsAll).all { rule ->
            update.indexOf(rule[0]) < update.indexOf(rule[1])
        }
    }
    println(correctlyOrdered.checksum())

    val fixed = (updates - correctlyOrdered.toSet()).map { update ->
        buildList {
            while (true) {
                val remainingRules = rules.filter(update::containsAll).filter { it[0] !in this }
                val page = (update - toSet()).singleOrNull { page -> page !in remainingRules.map { it[1] } } ?: break
                add(page)
            }
        }
    }
    println(fixed.checksum())
}
