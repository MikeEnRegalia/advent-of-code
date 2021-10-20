package aoc2018.day21

import aoc2018.day18.opcode

fun day21(input: String, part2: Boolean = false) = with(input.parseProgram()) {
    if (!part2) eval { it } else {
        val history = mutableSetOf<Int>()
        var last: Int? = null
        eval {
            if (!history.add(it)) last else {
                last = it; null
            }
        }
    }
}

private fun String.parseProgram() =
    substring(indexOf("\n") + 1).split("\n").map { it.split(" ") }
        .map { row -> row[0].opcode() to row.drop(1).map { it.toInt() } }
        .map { (opcode, args) -> Instruction(opcode, args[0], args[1], args[2]) }
        .let { Program(it, substring("#ip ".length, indexOf("\n")).toInt()) }

internal data class Instruction(val opcode: Opcode, val a: Int, val b: Int, val c: Int)
internal data class Program(val instructions: List<Instruction>, val ipReg: Int)
internal typealias Opcode = (MutableList<Int>, Int, Int) -> Int

private fun Program.eval(t: (Int) -> Int?): Int? {
    var ip = 0
    val r = mutableListOf(0, 0, 0, 0, 0, 0)
    while (ip in instructions.indices) {
        r[ipReg] = ip
        if (ip == 28) t(r[5])?.let { return it }
        with(instructions[ip]) { r[c] = opcode(r, a, b) }
        ip = r[ipReg] + 1
    }
    return null
}