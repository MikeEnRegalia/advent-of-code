package aoc2024

fun main() {
    fun List<String>.parse(sep: Char) = filter { sep in it }.map { it.split(sep).map(String::toInt) }
    val (rules, updates) = with(generateSequence(::readLine).toList()) { listOf('|', ',').map { parse(it) } }

    val correctlyOrdered = updates.filter { update ->
        rules.filter(update::containsAll).all { rule ->
            update.indexOf(rule[0]) < update.indexOf(rule[1])
        }
    }

    val fixed = (updates - correctlyOrdered.toSet()).map { update ->
        buildList {
            while (true) {
                val remainingRules = rules.filter(update::containsAll).filter { it[0] !in this }
                val page = (update - toSet()).singleOrNull { page -> page !in remainingRules.map { it[1] } } ?: break
                add(page)
            }
        }
    }

    listOf(correctlyOrdered, fixed).map { l -> l.sumOf { it[it.size / 2] } }.forEach(::println)
}
