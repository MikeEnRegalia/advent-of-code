package aoc2021b

fun main() {
    val (template, rules) = with(generateSequence(::readLine).toList()) {
        first() to drop(2).map { it.split(Regex(" -> ")) }
    }

    val (pairs, elements) = with(template) {
        listOf(windowed(2), map(Char::toString)).map { list ->
            list.groupingBy { it }.eachCount().mapValues { it.value.toLong() }.toMutableMap()
        }
    }

    fun result() = with(elements.values) { maxOf { it } - minOf { it } }

    repeat(40) { step ->
        val new = mutableMapOf<String, Long>()
        for ((rulePair, element) in rules) {
            pairs.entries.filter { it.key == rulePair }.forEach { (pair, q) ->
                pairs -= pair
                new.inc(pair[0] + element, q)
                new.inc(element + pair[1], q)
                elements.inc(element, q)
            }
        }
        new.forEach { pairs.inc(it.key, it.value) }
        if (step == 9) println(result())
    }
    println(result())
}

fun <K> MutableMap<K, Long>.inc(k: K, n: Long) = compute(k) { _, v -> if (v == null) n else v + n }