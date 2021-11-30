package aoc2018.day02

fun main() {
    val boxes = generateSequence(::readLine).toList()

    with(boxes.map { it.groupByCount() }) {
        val count1 = count { it.hasDuplicateValues(2) }
        val count2 = count { it.hasDuplicateValues(3) }
        (count1 * count2).also { println(it) }
    }

    (1 until boxes.minOf { it.length })
        .map { i -> boxes.map { it.withoutIndex(i) } }
        .map { it.groupByCount(2) }
        .filter { it.isNotEmpty() }
        .flatten()
        .forEach { println(it) }
}

private fun <K, V> Map<K, V>.filterKeysByValue(predicate: (V) -> Boolean) = filter { predicate(it.value) }.keys

private fun String.withoutIndex(i: Int) = substring(0, i) + substring(i + 1)

private fun CharSequence.groupByCount() = groupingBy { it }.eachCount()
private fun <T> Iterable<T>.groupByCount(min: Int) = groupingBy { it }.eachCount().filterKeysByValue { it == min }

private fun Map<Char, Int>.hasDuplicateValues(n: Int) = values.any { it == n }
