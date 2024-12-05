package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()

    val rules = lines
        .filter { '|' in it }
        .map { line -> line.split("|").map(String::toInt).let { it[0] to it[1] } }

    val updates = lines
        .filter { ',' in it }
        .map { it.split(",").map(String::toInt) }

    fun List<List<Int>>.checksum() = sumOf { it[it.size / 2] }

    val correctlyOrdered = updates.filter { update ->
        rules.filter { it.first in update && it.second in update }.all { rule ->
            update.indexOf(rule.first) < update.indexOf(rule.second)
        }
    }

    println(correctlyOrdered.checksum())

    val fixed = (updates - correctlyOrdered.toSet()).map { update ->
        val ordered = buildList {
            val remainingRules = rules.filter { it.first in update && it.second in update }.toMutableList()
            while (true) {
                val remainingPages = update - toSet()
                val smallest = remainingPages.singleOrNull { page -> page !in remainingRules.map { it.second } } ?: break
                add(smallest)
                remainingRules.removeAll { it.first == smallest }
            }
        }

        ordered.filter { it in update }
    }

    println(fixed.checksum())
}
