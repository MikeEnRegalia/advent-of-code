package aoc2018.day21

import aoc2018.day18.opcode

fun day21(input: String, part2: Boolean = false) =
    with(input.parseProgram()) { if (!part2) day21Part1() else day21Part2() }

private fun String.parseProgram() =
    substring(indexOf("\n") + 1).split("\n").map { it.split(" ") }
        .map { row -> row[0].opcode() to row.drop(1).map { it.toInt() } }
        .map { (opcode, args) -> Instruction(opcode, args[0], args[1], args[2]) }
        .let { Program(it, substring("#ip ".length, indexOf("\n")).toInt()) }

internal data class Instruction(val opcode: Opcode, val a: Int, val b: Int, val c: Int)
internal data class Program(val instructions: List<Instruction>, val ipReg: Int)
internal typealias Opcode = (MutableList<Int>, Int, Int) -> Int

private fun Program.day21Part1(): Int? {
    var ip = 0
    val r = mutableListOf(0, 0, 0, 0, 0, 0)
    while (ip in instructions.indices) {
        r[ipReg] = ip
        if (ip == 28) return r[5]
        with(instructions[ip]) { r[c] = opcode(r, a, b) }
        ip = r[ipReg] + 1
    }
    return null
}

private fun Program.day21Part2(): Int {
    var last = 0
    val seenBefore = mutableSetOf<Int>()
    var ip = 0
    val r = mutableListOf(0, 0, 0, 0, 0, 0)
    while (ip in instructions.indices) {
        r[ipReg] = ip
        if (ip == 28) {
            if (!seenBefore.add(r[5])) {
                return last
            } else last = r[5]
        }
        with(instructions[ip]) { r[c] = opcode(r, a, b) }
        ip = r[ipReg] + 1
    }
    return last
}