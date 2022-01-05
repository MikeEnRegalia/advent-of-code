package aoc2019

fun main() {
    val input = readln().split(",").map(String::toLong).toMutableList()

    fun List<Long>.mod(noun: Int, verb: Int): List<Long> = toMutableList().apply {
        this[1] = noun.toLong()
        this[2] = verb.toLong()
    }

    runIntCode(input.mod(12, 2)).also(::println)

    for (noun in 0..99) for (verb in 0..99)
        if (19690720L == runIntCode(input.mod(noun, verb))) println(noun * 100 + verb)
}

fun runIntCode(
    program: List<Long>,
    input: () -> Long = { throw IllegalStateException() },
    output: (Long) -> Unit = ::println
): Long? {
    val code = program.mapIndexed { i, x -> i.toLong() to x }.toMap().toMutableMap()
    var pos = 0L
    var relativeBase = 0L
    while (true) {
        val instruction = code[pos].toString().padStart(5, '0')
        val modes = instruction.substring(0, 3).reversed()

        fun p(n: Int) = code[pos + 1 + n]!!.let {
            when (modes[n].digitToInt()) {
                0 -> code.getOrDefault(it, 0)
                1 -> it
                else -> code.getOrDefault(relativeBase + it, 0)
            }
        }

        fun w(n: Int, v: Long) = when (modes[n].digitToInt()) {
            0 -> code[code[pos + 1 + n]!!] = v
            else -> code[relativeBase + code[pos + 1 + n]!!] = v
        }

        when (val opcode = instruction.takeLast(2).toInt()) {
            1, 2 -> {
                val a = p(0)
                val b = p(1)
                w(2, if (opcode == 1) a.plus(b) else a.times(b))
                pos += 4
            }
            3 -> {
                w(0, input())
                pos += 2
            }
            4 -> output(p(0)).also { pos += 2 }
            5 -> pos = if (p(0) != 0L) p(1) else pos + 3
            6 -> pos = if (p(0) == 0L) p(1) else pos + 3
            7 -> {
                w(2, if (p(0) < p(1)) 1 else 0)
                pos += 4
            }
            8 -> {
                w(2, if (p(0) == p(1)) 1 else 0)
                pos += 4
            }
            9 -> {
                relativeBase += p(0)
                pos += 2
            }
            99 -> return code[0]
        }
    }
}