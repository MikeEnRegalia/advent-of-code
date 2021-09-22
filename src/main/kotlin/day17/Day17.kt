package day17

fun main() {
    val combos = mutableSetOf<String>()
    combinations(input(), total = 150) { combos.add(it.joinToString("")) }.also { println(it) }
    println(combos.count { combo -> combo.length == combos.sortedBy { it.length }[0].length })
}

fun combinations(list: List<Int>, total: Int, prefix: List<Int> = listOf(), f: (List<Int>) -> Unit): Int {
    val sum = list.sum()
    return when {
        sum < total -> 0
        sum == total -> 1.also { f(prefix.plus(list)) }
        list.isEmpty() -> 0
        else -> {
            val first = list.first()
            val remaining = list.drop(1)
            combinations(remaining, total - first, prefix.plus(first), f) + combinations(remaining, total, prefix, f)
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