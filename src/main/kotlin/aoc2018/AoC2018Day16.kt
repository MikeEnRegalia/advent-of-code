package aoc2018

import aoc2018.AocDay16.runProgram
import aoc2018.AocDay16.solve
import aoc2018.AocDay16.toOpcodeCandidates
import aoc2018.AocDay16.toProgram

fun day16ChronalClassificationPart1(input: String): Int = solve(input.toOpcodeCandidates())

fun day16ChronalClassificationPart2(input: String): Int = runProgram(input.toOpcodeCandidates(), input.toProgram())

internal typealias Opcode = (MutableList<Int>, Int, Int, Int) -> Unit

internal object AocDay16 {
    fun String.toOpcodeCandidates() = split(Regex("\n\n\n\n"))[0]
        .split(Regex("\n\n"))
        .map { it.split("\n") }
        .map {
            OpCodeCandidate(
                it[0].bracketContents().toOpCodeCandidate(),
                it[1].toOpCodeCandidate(),
                it[2].bracketContents().toOpCodeCandidate()
            )
        }

    data class OpCodeCandidate(val before: List<Int>, val command: List<Int>, val after: List<Int>)

    fun String.toOpCodeCandidate() = split(Regex(if (contains(",")) ", " else " ")).map { it.toInt() }
    fun String.bracketContents() = substring(indexOf("[") + 1, indexOf("]"))

    val opcodes = listOf<Opcode>(
        { r, a, b, c -> r[c] = r[a] + r[b] },
        { r, a, b, c -> r[c] = r[a] + b },
        { r, a, b, c -> r[c] = r[a] * r[b] },
        { r, a, b, c -> r[c] = r[a] * b },
        { r, a, b, c -> r[c] = r[a].and(r[b]) },
        { r, a, b, c -> r[c] = r[a].and(b) },
        { r, a, b, c -> r[c] = r[a].or(r[b]) },
        { r, a, b, c -> r[c] = r[a].or(b) },
        { r, a, _, c -> r[c] = r[a] },
        { r, a, _, c -> r[c] = a },
        { r, a, b, c -> r[c] = if (a > r[b]) 1 else 0 },
        { r, a, b, c -> r[c] = if (r[a] > b) 1 else 0 },
        { r, a, b, c -> r[c] = if (r[a] > r[b]) 1 else 0 },
        { r, a, b, c -> r[c] = if (a == r[b]) 1 else 0 },
        { r, a, b, c -> r[c] = if (r[a] == b) 1 else 0 },
        { r, a, b, c -> r[c] = if (r[a] == r[b]) 1 else 0 },
    )

    fun solve(candidates: List<OpCodeCandidate>) = candidates.count { c ->
        c.run {
            opcodes.count { opcode ->
                before.toMutableList().also { opcode(it, command[1], command[2], command[3]) } == after
            } >= 3
        }
    }

    fun String.toProgram() = split(Regex("\n\n\n\n"))[1]
        .split("\n").map { row ->
            row.split(" ").map { it.toInt() }
        }
        .map { Instruction(it[0], it[1], it[2], it[3]) }

    data class Instruction(val opcodeNumber: Int, val a: Int, val b: Int, val c: Int)

    fun runProgram(candidates: List<OpCodeCandidate>, program: List<Instruction>): Int {
        val opcodesByNumber = identifyOpcodes(candidates)

        println(opcodesByNumber.keys)

        val r = mutableListOf(0, 0, 0, 0)
        program.forEach { (opcodeNumber, a, b, c) ->
            val opcode =
                opcodesByNumber[opcodeNumber] ?: throw IllegalArgumentException(opcodeNumber.toString())
            opcode(r, a, b, c)
        }
        return r[0]
    }


    private fun identifyOpcodes(candidates: List<OpCodeCandidate>): Map<Int, Opcode> {
        val allOpcodeNumbers = candidates.map { it.command[0] }.distinct().toSet()
        val result = mutableMapOf<Int, Opcode>()
        while (true) {
            val newlyFound = allOpcodeNumbers
                .filterNot { it in result.keys }
                .mapNotNull { opcodeNumber ->
                    opcodes
                        .filterNot { it in result.values }
                        .filter { opcode ->
                            candidates.filter { it.command[0] == opcodeNumber }.all { test(it, opcode) }
                        }
                        .takeIf { it.size == 1 }
                        ?.first()
                        ?.let { opcodeNumber to it }
                }
            if (newlyFound.isEmpty()) return result
            newlyFound.forEach { (n, op) -> result[n] = op }
        }
    }

    private fun test(c: OpCodeCandidate, opcode: Opcode) =
        c.run { before.toMutableList().also { opcode(it, command[1], command[2], command[3]) } == after }
}
