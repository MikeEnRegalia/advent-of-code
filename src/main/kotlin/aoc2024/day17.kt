package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    val regInit = lines.take(3).map { it.filter(Char::isDigit).toInt().toString(8).map(Char::digitToInt) }
    val program = lines[4].split(",").map { it.filter(Char::isDigit).toInt() }
    fun List<Int>.toLong(radix: Int) = joinToString("").toLong(radix)

    fun compute(regAInit: List<Int>): List<Int> {
        var ip = 0
        var regA = regAInit.toLong(8)
        var regB = 0L
        var regC = 0L

        val out = mutableListOf<Int>()
        while (ip in program.indices) {
            val (op, arg) = program[ip++] to program[ip++]
            val comboOp = when (arg) {
                0, 1, 2, 3 -> arg.toLong()
                4 -> regA
                5 -> regB
                6 -> regC
                else -> throw IllegalArgumentException(arg.toString())
            }

            fun denominator() = if (comboOp <= 0L) 1L else (0L until comboOp).map { 2L }.reduce(Long::times)

            when (op) {
                0 -> regA /= denominator()
                1 -> regB = regB xor arg.toLong()
                2 -> regB = comboOp % 8
                3 -> if (regA != 0L) ip = arg
                4 -> regB = regB xor regC
                5 -> out += (comboOp % 8).toInt()
                6 -> regB = regA / denominator()
                7 -> regC = regA / denominator()
            }
        }
        return out
    }

    println(compute(regInit[0]).joinToString(","))

    fun solve(out: List<Int>, prev: List<Int> = listOf()): List<Int>? = if (out.isEmpty()) prev else
        (0..7).filter { compute(prev + listOf(it)).first() == out.last() }
            .mapNotNull { d -> solve(out.dropLast(1), prev + listOf(d))?.let { d to it } }
            .minByOrNull { it.first }?.second

    println(solve(program)!!.toLong(8))
}




