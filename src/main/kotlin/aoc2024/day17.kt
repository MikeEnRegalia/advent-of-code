package aoc2024

fun main() {
    val lines = generateSequence(::readLine).toList()
    val regInit = lines.take(3).map { it.filter(Char::isDigit).toLong() }
    val program = lines[4].split(",").map { it.filter(Char::isDigit).toInt() }

    fun compute(regAInit: Long, expectedOut: List<Long>? = null): List<Long> {
        var ip = 0
        var regA = regAInit
        var regB = 0L
        var regC = 0L

        val out = mutableListOf<Long>()
        while (ip in program.indices) {
            val (op, arg) = program[ip++] to program[ip++]
            val comboOperand = when (arg) {
                0, 1, 2, 3 -> arg.toLong()
                4 -> regA
                5 -> regB
                6 -> regC
                else -> throw IllegalArgumentException(arg.toString())
            }

            fun potOperand() = if (comboOperand <= 0L) 1L else (0L until comboOperand).map { 2L }.reduce(Long::times)

            when (op) {
                0 -> regA /= potOperand()
                1 -> regB = regB xor arg.toLong()
                2 -> regB = comboOperand % 8
                3 -> if (regA != 0L) ip = arg
                4 -> regB = regB xor regC
                5 -> out += comboOperand % 8
                6 -> regB =
                    regA / potOperand()

                7 -> {
                    val denominator = potOperand()
                    if (denominator == 0L) throw IllegalStateException(comboOperand.toString())
                    regC = regA / denominator
                }
            }
            if (out.isNotEmpty() && expectedOut != null && expectedOut.take(out.size) != out)
                return listOf()
        }
        return out
    }

    println(compute(regInit[0]).joinToString(","))

    fun transform(origA: Long, origCode: LongArray): Boolean {
        var A = origA
        var i = 0
        do {
            val B = (A % 8) xor 1
            var C = A
            for (j in 0 until B) C /= 2
            val res = (B xor 5 xor C) % 8
            if (i >= origCode.size || res != origCode[i]) return false
            A /= 8
            i++
        } while (A > 0)
        return true
    }

    val digits = mutableListOf<Int>()
    for ((i1, i2, i3) in program.reversed().indices.windowed(3)) {
        val permutations = (0..7).filter { i1 !in digits.indices || it == digits[i1]}.flatMap { a ->
            (0..7).filter { i2 !in digits.indices || it == digits[i2]}.flatMap { b ->
                (0..7).filter { i3 !in digits.indices || it == digits[i3]}.map { c -> listOf(a, b, c) }} }
        val matching = permutations.map {
            digits.take(i1) + it
        }.filter { c ->

            val result = compute((c + IntArray(program.size - i1 - c.size) { 0 }.toList()).joinToString("").toLong(8)).map { it.toInt() }
            val reversedResult = result.reversed()
            val reversedProgram = program.reversed()
            //println("${c.joinToString("")} -> ${reversedResult.joinToString("")} vs ${reversedProgram.joinToString("")}")

            reversedProgram.take(i1 + 1) == reversedResult.take(i1 + 1)
        }
        val prefixes = matching.map { it[i1] }.distinct()
        println("matching prefixes for $i1: $matching -> $prefixes")
    }
}





