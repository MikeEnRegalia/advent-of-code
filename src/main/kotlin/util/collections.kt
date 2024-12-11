package util

fun <T> Iterable<T>.dropAllAfter(p: (T) -> Boolean) = buildList {
    for (t in this@dropAllAfter) {
        add(t)
        if (p(t)) break
    }
}

fun <T> Iterable<T>.takeAllBefore(p: (T) -> Boolean) = buildList {
    for (t in this@takeAllBefore) {
        if (p(t)) break
        add(t)
    }
}

fun <K> Collection<K>.mapToCount() = groupingBy { it }.eachCount()
fun <K> Collection<K>.mapToCountAsLong() = mapToCount().mapValues { it.value.toLong() }
fun <K> MutableMap<K, Int>.incrementValue(key: K, value: Int) = compute(key) { _, oldV -> (oldV ?: 0) + value }
fun <K> MutableMap<K, Long>.incrementValue(key: K, value: Long) = compute(key) { _, oldV -> (oldV ?: 0) + value }
