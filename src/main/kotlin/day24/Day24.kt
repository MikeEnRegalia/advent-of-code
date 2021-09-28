package day24

fun main() {
    with(input().sortedDescending()) {
        byThree()
        byFour()
    }
}

private fun List<Int>.byThree() {
    val groupSum = sum() / 3
    var minQE: Long? = null
    for (n in 1..size) {
        nCombos(n, groupSum)
            .map { it to it.multiplied() }
            .filter { (_, qe) -> minQE?.let { min -> qe < min } ?: true }
            .mapNotNull { (firstGroup, qe) ->
                with(minus(firstGroup)) {
                    qe.takeIf {
                        (1..size / 2).asSequence()
                            .mapNotNull { nCombos(it, groupSum).firstOrNull() }
                            .any()
                    }
                        ?.also { minQE = qe }
                }
            }
            .sorted()
            .firstOrNull()
            ?.also { println("$it"); return }
    }
}

private fun List<Int>.byFour() {
    val groupSum = sum() / 4

    for (n in 1..size) {
        nCombos(n, groupSum)
            .mapNotNull { firstGroup ->
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
                            }
                            .firstOrNull()
                    }
                    .firstOrNull()
            }
            .sortedBy { it[0].multiplied() }
            .firstOrNull()
            ?.also { groups ->
                println(groups[0].multiplied().toString())
                return
            }
    }
}

fun Iterable<Int>.multiplied() = fold(1L) { acc, x -> acc * x }

fun List<Int>.nCombos(n: Int, sum: Int): Sequence<List<Int>> = sum()
    .let { mySum ->
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
113"""
    .split("\n")
    .map { it.toInt() }