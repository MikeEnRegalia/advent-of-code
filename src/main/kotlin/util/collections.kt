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