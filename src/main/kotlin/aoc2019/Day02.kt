package aoc2019

fun main() {
    val input = readln().split(",").map(String::toInt).toMutableList()

    fun List<Int>.mod(noun: Int, verb: Int): List<Int> = toMutableList().apply {
        this[1] = noun
        this[2] = verb
    }

    runIntCode(input.mod(12, 2)).also(::println)

    for (noun in 0..99) for (verb in 0..99)
        if (19690720 == runIntCode(input.mod(noun, verb))) println(noun * 100 + verb)
}

fun runIntCode(input: List<Int>): Int? {
    val code = input.mapIndexed { i, x -> i to x }.toMap().toMutableMap()
    var pos = 0
    while (true) {
        when (val opcode = code[pos]) {
            1, 2 -> {
                val a = code.getOrDefault(code[pos + 1]!!, 0)
                val b = code.getOrDefault(code[pos + 2]!!, 0)
                code[code[pos + 3]!!] = if (opcode == 1) a.plus(b) else a.times(b)
                pos += 4
            }
            99 -> return code[0]
        }
    }
}