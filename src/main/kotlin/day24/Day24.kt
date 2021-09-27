package day24

fun main() {
    with(input().sortedDescending()) {
        byThree()
        byFour()
    }
}

private fun List<Int>.byThree() {
    val groupSum = sum() / 3
    for (n in 1..size) {
        nCombos(n, groupSum)
            .map { firstGroup ->
                val remaining = minus(firstGroup)
                (1..remaining.size).firstNotNullOf {
                    remaining.nCombos(it, groupSum).firstOrNull()
                }.let {
                    listOf(firstGroup, it, remaining.minus(it))
                }
            }
            .forEach { groups ->
                assert(groups.all { it.sum() == groupSum })
                println("${groups[0].fold(1L) { acc, it -> acc * it }}")
                return
            }
    }
}

private fun List<Int>.byFour() {
    val groupSum = sum() / 4

    for (n in 1..size) {
        println("checking $n")
        nCombos(n, groupSum)
            .mapNotNull { firstGroup ->
                //println("candidate: $firstGroup")
                val remaining = minus(firstGroup)
                (1..remaining.size).asSequence()
                    .mapNotNull { o ->
                        remaining.nCombos(o, groupSum)
                            .mapNotNull { secondGroup ->
                                val moreRemaining = remaining.minus(secondGroup)
                                (1..moreRemaining.size / 2).asSequence()
                                    .mapNotNull {
                                        moreRemaining.nCombos(it, groupSum).firstOrNull()
                                    }
                                    .firstOrNull()
                                    ?.let { thirdGroup ->
                                        listOf(
                                            firstGroup,
                                            secondGroup,
                                            thirdGroup,
                                            moreRemaining.minus(thirdGroup)
                                        )
                                    }
                                //?.also { println(it) }
                            }
                            .firstOrNull()
                        //?.also { println(it) }
                    }
                    .firstOrNull()
                    ?.also { println(it) }
            }
            .sortedBy { it[0].fold(1L) { acc, x -> acc * x } }
            .firstOrNull()
            ?.also { groups ->
                assert(groups.all { it.sum() == groupSum })
                println("$n: ${groups[0].fold(1L) { acc, it -> acc * it }}")
                return
            }
    }
}

fun List<Int>.nCombos(n: Int, sum: Int): Sequence<List<Int>> = sum().let { mySum ->
    if (mySum < sum) sequenceOf()
    else if (n == 1) asSequence().filter { it == sum }.map { listOf(it) }
    else asSequence()
        .filter { mySum - it >= sum }
        .flatMap { i -> minus(i).nCombos(n - 1, sum - i).map { it.plus(i) } }
}

fun input() = """1
2
3
5
7
13
17
19
23
29
31
37
41
43
53
59
61
67
71
73
79
83
89
97
101
103
107
109
113""".split("\n").map { it.toInt() }