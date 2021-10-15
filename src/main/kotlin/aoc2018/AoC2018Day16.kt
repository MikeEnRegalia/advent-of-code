package aoc2018

import aoc2018.AocDay16.eval
import aoc2018.AocDay16.identifyOpcodes
import aoc2018.AocDay16.solve
import aoc2018.AocDay16.toOpcodeCandidates
import aoc2018.AocDay16.toProgram

fun String.day16ChronalClassificationPart1(): Int = toOpcodeCandidates().solve()
fun String.day16ChronalClassificationPart2(): Int = toProgram().eval(toOpcodeCandidates().identifyOpcodes())

internal typealias Opcode = (MutableList<Int>, Int, Int, Int) -> Unit

internal object AocDay16 {
    fun String.toOpcodeCandidates() = split(Regex("\n\n\n\n"))[0]
        .split(Regex("\n\n"))
        .map { paragraph -> paragraph.split("\n").map { it.unwrapBrackets().toIntList() } }
        .map { OpCodeCandidate(it[0], it[1], it[2]) }

    fun String.unwrapBrackets() = if (!contains("[")) this else substring(indexOf("[") + 1, indexOf("]"))
    fun String.toIntList() = split(Regex(if (contains(",")) ", " else " ")).map { it.toInt() }

    data class OpCodeCandidate(val before: List<Int>, val cmd: List<Int>, val after: List<Int>)

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

    fun List<OpCodeCandidate>.solve() = count { c -> opcodes.count { c.resultOf(it) } >= 3 }

    fun String.toProgram() = split(Regex("\n\n\n\n"))[1]
        .split("\n").map { row -> row.split(" ").map { it.toInt() } }
        .map { Instruction(it[0], it[1], it[2], it[3]) }

    data class Instruction(val opcodeNumber: Int, val a: Int, val b: Int, val c: Int)

    fun List<Instruction>.eval(opcodes: Map<Int, Opcode>) =
        mutableListOf(0, 0, 0, 0).let { r -> forEach { (n, a, b, c) -> (opcodes[n]!!)(r, a, b, c) }; r[0] }

    internal fun List<OpCodeCandidate>.identifyOpcodes(): Map<Int, Opcode> {
        val allNumbers = map { it.cmd[0] }.distinct().toSet()
        val result = mutableMapOf<Int, Opcode>()
        while (true) {
            val newlyFound = allNumbers.mapNotNull { n ->
                filter { it.cmd[0] == n }.findOpcode(result.values)?.let { n to it }
            }
            if (newlyFound.isEmpty()) return result
            result.putAll(newlyFound)
        }
    }

    private fun List<OpCodeCandidate>.findOpcode(known: Collection<Opcode>) =
        opcodes
            .filterNot { it in known }
            .filter { opcode -> all { it.resultOf(opcode) } }
            .takeIf { it.size == 1 }
            ?.first()


    private fun OpCodeCandidate.resultOf(opcode: Opcode) =
        before.toMutableList().also { opcode(it, cmd[1], cmd[2], cmd[3]) } == after
}
