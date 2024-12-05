package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    val rules = lines.filter { '|' in it }.map { it.split("|").map(String::toInt) }
    val updates = lines.filter { ',' in it }.map { it.split(",").map(String::toInt) }

    fun List<List<Int>>.checksum() = sumOf { it[it.size / 2] }

    val correctlyOrdered = updates.filter { update ->
        rules.filter(update::containsAll).all { rule ->
            update.indexOf(rule[0]) < update.indexOf(rule[1])
        }
    }

    println(correctlyOrdered.checksum())

    val fixed = (updates - correctlyOrdered.toSet()).map { update ->
        val ordered = buildList {
            val remainingRules = rules.filter(update::containsAll).toMutableList()
            while (true) {
                val remainingPages = update - toSet()
                val smallest = remainingPages.singleOrNull { page -> page !in remainingRules.map { it[1] } } ?: break
                add(smallest)
                remainingRules.removeAll { it[0] == smallest }
            }
        }

        ordered.filter { it in update }
    }

    println(fixed.checksum())
}
