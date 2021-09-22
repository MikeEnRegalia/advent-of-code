package day17

fun main() {
    combinations(input(), total = 150).also { println(it) }
}

fun combinations(list: List<Int>, total: Int): Int {
    val sum = list.sum()
    if (sum < total) return 0
    if (sum == total) return 1
    if (list.isEmpty()) return 0

    val first = list.first()
    val remaining = list.drop(1)
    return combinations(remaining, total - first) + combinations(remaining, total)
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