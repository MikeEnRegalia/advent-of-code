package aoc2021b

fun main() = generateSequence(::readLine).day08().forEach(::println)

fun Sequence<String>.day08(): Iterable<Any?> {
    val input = map { row -> row.split(" \\| ".toRegex()).map { it.split(" ") } }.toList()

    val part1 = input.sumOf { (_, output) -> output.count { listOf(2, 3, 4, 7).contains(it.length) } }

    val part2 = input.map { (tokens, output) -> tokens.map { it.toSet() }.asSequence() to output.map { it.toSet() } }
        .sumOf { (tokens, output) ->
            val one = tokens.single { it.size == 2 }
            val seven = tokens.single { it.size == 3 }
            val a = seven.subtract(one).single()

            val four = tokens.single { it.size == 4 }
            val g = tokens.filter { it.size == 6 }.map { it.minus(a) }.single { it.containsAll(four) }
                .minus(four).single()

            val d = tokens.filter { it.size == 5 }.single { it.containsAll(setOf(a, g).plus(one)) }
                .minus(setOf(a, g).plus(one)).single()

            val three = tokens.filter { it.size == 5 }.single { it.containsAll(setOf(a, d, g).plus(one)) }

            val five = tokens.filter { it.size == 5 }.filter { it != three }
                .filter { it.minus(four) == setOf(a, g) }.single()

            val b = five.minus(setOf(a, d, g)).minus(one).single()

            val two = tokens.filter { it.size == 5 }.minus(setOf(five, three)).single()

            val e = two.minus(four).minus(a).minus(g).single()
            val f = five.minus(setOf(a, b, d, g)).single()
            val c = tokens.single { it.size == 7 }.minus(setOf(a, b, d, e, f, g)).single()

            output.mapNotNull {
                when (it) {
                    setOf(a, b, c, e, f, g) -> 0
                    one -> 1
                    two -> 2
                    three -> 3
                    four -> 4
                    five -> 5
                    setOf(a, b, d, e, f, g) -> 6
                    seven -> 7
                    setOf(a, b, c, d, e, f, g) -> 8
                    setOf(a, b, c, d, f, g) -> 9
                    else -> null
                }
            }.joinToString("") { it.toString() }.toInt()
        }

    return listOf(part1, part2)
}