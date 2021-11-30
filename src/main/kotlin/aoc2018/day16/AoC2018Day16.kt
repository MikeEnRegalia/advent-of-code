package aoc2018.day16

import aoc2018.day16.AocDay16.eval
import aoc2018.day16.AocDay16.identifyOpcodes
import aoc2018.day16.AocDay16.probe
import aoc2018.day16.AocDay16.toOpcodeCandidates
import aoc2018.day16.AocDay16.toProgram

fun String.day16ChronalClassificationPart1(): Int = toOpcodeCandidates().probe()
fun String.day16ChronalClassificationPart2(): Int = toProgram().eval(toOpcodeCandidates().identifyOpcodes())

internal typealias Opcode = MutableList<Int>.(Int, Int) -> Int

internal object AocDay16 {
    fun String.toOpcodeCandidates() = split(Regex("\n\n\n\n"))[0]
        .split(Regex("\n\n"))
        .map { paragraph -> paragraph.split("\n").map { it.unwrapBrackets().toIntList() } }
        .map { OpCodeCandidate(it[0], it[1], it[2]) }

    fun String.unwrapBrackets() = if (contains("[")) substring(indexOf("[") + 1, indexOf("]")) else this
    fun String.toIntList() = split(Regex(if (contains(",")) ", " else " ")).map { it.toInt() }

    data class OpCodeCandidate(val before: List<Int>, val cmd: List<Int>, val after: List<Int>)

    val opcodes = listOf<Opcode>(
        { a, b -> this[a] + this[b] },
        { a, b -> this[a] + b },
        { a, b -> this[a] * this[b] },
        { a, b -> this[a] * b },
        { a, b -> this[a].and(this[b]) },
        { a, b -> this[a].and(b) },
        { a, b -> this[a].or(this[b]) },
        { a, b -> this[a].or(b) },
        { a, _ -> this[a] },
        { a, _ -> a },
        { a, b -> if (a > this[b]) 1 else 0 },
        { a, b -> if (this[a] > b) 1 else 0 },
        { a, b -> if (this[a] > this[b]) 1 else 0 },
        { a, b -> if (a == this[b]) 1 else 0 },
        { a, b -> if (this[a] == b) 1 else 0 },
        { a, b -> if (this[a] == this[b]) 1 else 0 },
    )

    fun List<OpCodeCandidate>.probe() = count { c -> opcodes.count { c.isResultOf(it) } >= 3 }

    fun String.toProgram() = split(Regex("\n\n\n\n"))[1]
        .split("\n").map { row -> row.split(" ").map { it.toInt() } }
        .map { Instruction(it[0], it[1], it[2], it[3]) }

    data class Instruction(val opcodeNumber: Int, val a: Int, val b: Int, val c: Int)

    fun List<Instruction>.eval(opcodes: Map<Int, Opcode>) =
        mutableListOf(0, 0, 0, 0).let { r -> forEach { (n, a, b, c) -> r[c] = r.(opcodes[n]!!)(a, b) }; r[0] }

    internal fun List<OpCodeCandidate>.identifyOpcodes(): Map<Int, Opcode> {
        val allNumbers = map { it.cmd[0] }.distinct().toSet()
        val result = mutableMapOf<Int, Opcode>()
        while (true) {
            val opcodes = opcodes.filterNot { it in result.values }
            val newlyFound = allNumbers.mapNotNull { n -> opcodes.match(filter { it.cmd[0] == n })?.let { n to it } }
            if (newlyFound.isNotEmpty()) result.putAll(newlyFound) else return result
        }
    }

    private fun Collection<Opcode>.match(candidatesSameN: List<OpCodeCandidate>) =
        filter { opcode -> candidatesSameN.all { it.isResultOf(opcode) } }.takeIf { it.size == 1 }?.first()

    private fun OpCodeCandidate.isResultOf(opcode: Opcode) =
        before.toMutableList().also { r -> r[cmd[3]] = r.opcode(cmd[1], cmd[2]) } == after
}