package aoc2024

import kotlin.math.pow

fun main() {

    val lines = generateSequence(::readLine).toList()
    val regInit = lines.take(3).map { it.filter { it.isDigit() }.toInt() }

    val program = lines[4].split(",").map {
        it.filter { it.isDigit() }.toInt()
    }

    var ip = 0
    val reg = regInit.toIntArray()

    fun comboOperand(i: Int) = when (i) {
        0, 1, 2, 3 -> i
        4, 5, 6 -> reg[i - 4]
        else -> throw IllegalArgumentException(i.toString())
    }

    val out = mutableListOf<Int>()
    while (ip in program.indices) {
        val (op, arg) = program[ip++] to program[ip++]
        val comboOperand = comboOperand(arg)
        when (op) {
            0 -> reg[0] = reg[0] / 2.toDouble().pow(comboOperand).toInt()
            1 -> reg[1] = reg[1].xor(arg)
            2 -> reg[1] = comboOperand % 8
            3 -> if (reg[0] != 0) ip = arg
            4 -> reg[1] = reg[1] xor reg[2]
            5 -> out += comboOperand % 8
            6 -> reg[1] = reg[0] / 2.toDouble().pow(comboOperand).toInt()
            7 -> reg[2] = reg[0] / 2.toDouble().pow(comboOperand).toInt()
        }
    }

    println(out.joinToString(",") { it.toString() })
}
