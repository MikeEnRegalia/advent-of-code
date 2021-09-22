package day17

fun main() {
    combinations2(listOf(20, 15, 10, 5, 5), total = 25).also { println(it) }
    //solve(input(), total = 150).also { println(it) }
}

fun solve(input: Collection<Int>, total: Int): Int {
    val containers = input.sorted().reversed()
    val containerCounts = containers.groupingBy { it }.eachCount()

    return mutableSetOf<Collection<Int>>()
        .apply { combinations(containers, total) }
        .run {
            val extra = containerCounts.entries.filter { it.value > 1 }
                .sumOf { (size, times) -> count { it.count { c -> c == size } == 1 } * (times - 1) }
            size + extra
        }
}

fun MutableSet<Collection<Int>>.combinations(list: Collection<Int>, total: Int) {
    val sum = list.sum()
    if (sum == total) {
        add(list)
    } else if (sum > total) {
        list.distinct().forEach { combinations(list.minus(it), total) }
    }
}

fun combinations2(list: List<Int>, total: Int): Int {
    val sum = list.sum()
    if (sum == total) return 1.also { println(list) }
    else if (sum < total) return 0
    if (list.size == 1) return 0
    if (list.size == 2) return if (list.any { it == total }) 1.also { println(list) } else 0
    // at least three elements
    val a = list[0]
    val b = list[1]

    return combinations2(list.minus(a).minus(b), total) +
            combinations2(list.minus(a), total) +
            combinations2(list.minus(b), total) +
            combinations2(list.minus(a).minus(b).plus(a + b), total)
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