package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    val regInit = lines.take(3).map { it.substringAfterLast(" ").toInt().toString(8) }
    val program = lines[4].split(",").map { it.filter(Char::isDigit).toInt() }

    fun compute(regAInit: String): String {
        var p = 0
        var regA = regAInit.toLong(8)
        var regB = 0L
        var regC = 0L

        var out = ""
        while (p in program.indices) {
            val (op, arg) = program[p++] to program[p++]
            val comboOp = when (arg) {
                0, 1, 2, 3 -> arg.toLong()
                4 -> regA
                5 -> regB
                6 -> regC
                else -> throw IllegalArgumentException(arg.toString())
            }

            fun denominator() = if (comboOp == 0L) 1L else (0L until comboOp).map { 2L }.reduce(Long::times)

            when (op) {
                0 -> regA /= denominator()
                1 -> regB = regB xor arg.toLong()
                2 -> regB = comboOp % 8
                3 -> if (regA != 0L) p = arg
                4 -> regB = regB xor regC
                5 -> out += (comboOp % 8).toInt()
                6 -> regB = regA / denominator()
                7 -> regC = regA / denominator()
            }
        }
        return out
    }

    println(compute(regInit[0]).map { it.digitToInt()}.joinToString(","))

    fun solve(out: List<Int>, prev: String = ""): String? = if (out.isEmpty()) prev else
        (0..7).filter { compute(prev + it).first().digitToInt() == out.last() }
            .mapNotNull { d -> solve(out.dropLast(1), prev + d)?.let { d to it } }
            .minByOrNull { it.first }?.second

    println(solve(program)!!.toLong(8))
}




