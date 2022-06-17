package aoc2021b

fun runViaStdInOut(solution: List<String>.() -> Pair<Any?, Any?>) =
    generateSequence(::readLine).toList()
        .solution()
        .let { (part1, part2) ->
            println(part1)
            println(part2)
        }
