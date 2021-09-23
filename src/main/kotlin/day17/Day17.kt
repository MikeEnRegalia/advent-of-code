package day17

fun main() {
    val combos = mutableSetOf<List<Int>>()
    combinations(input(), total = 150) { combos.add(it) }.also { println(it) }

    val minCombo = combos.minOf { it.size }
    println(combos.count { it.size == minCombo })
}

fun combinations(list: List<Int>, total: Int, prefix: List<Int> = listOf(), f: (List<Int>) -> Unit): Int {
    val sum = list.sum()
    return when {
        sum < total -> 0
        sum == total -> 1.also { f(prefix.plus(list)) }
        else -> {
            if (list.isEmpty()) return 0

            val first = list.first()
            val remaining = list.minus(first)
            val includingFirst = combinations(remaining, total - first, prefix.plus(first), f)
            val excludingFirst = combinations(remaining, total, prefix, f)
            includingFirst + excludingFirst
        }
    }
}

fun input() = """11
30
47
31
32
36
3
1
5
3
32
36
15
11
46
26
28
1
19
3""".split("\n").map { it.toInt() }