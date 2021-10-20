package aoc2018.day19

fun day19(input: String, r0: Int = 0): Int {
    val ipReg = input.substring("#ip ".length, input.indexOf("\n")).toInt()
    val program = input.substring(input.indexOf("\n") + 1).split("\n")
        .map { it.split(" ") }
        .map { row -> row[0].opcode() to row.drop(1).map { it.toInt() } }
        .map { (opcode, args) -> Instruction(opcode, args[0], args[1], args[2]) }

    var ip = 0
    val r = mutableListOf(r0, 0, 0, 0, 0, 0)
    while (ip in program.indices) {
        r[ipReg] = ip
        with(program[ip]) { r[c] = opcode(r, a, b) }
        ip = r[ipReg] + 1
        if (r0 == 1 && r[0] != 1) return r[2].divisors().sum()
    }
    return r[0]
}

private fun Int.divisors() = (1..this).filter { this % it == 0 }

internal data class Instruction(val opcode: Opcode, val a: Int, val b: Int, val c: Int)

internal typealias Opcode = (MutableList<Int>, Int, Int) -> Int

fun String.opcode(): Opcode = when (this) {
    "addr" -> { r, a, b -> r[a] + r[b] }
    "addi" -> { r, a, b -> r[a] + b }
    "mulr" -> { r, a, b -> r[a] * r[b] }
    "muli" -> { r, a, b -> r[a] * b }
    "banr" -> { r, a, b -> r[a].and(r[b]) }
    "bani" -> { r, a, b -> r[a].and(b) }
    "borr" -> { r, a, b -> r[a].or(r[b]) }
    "bori" -> { r, a, b -> r[a].or(b) }
    "setr" -> { r, a, _ -> r[a] }
    "seti" -> { _, a, _ -> a }
    "gtir" -> { r, a, b -> if (a > r[b]) 1 else 0 }
    "gtri" -> { r, a, b -> if (r[a] > b) 1 else 0 }
    "gtrr" -> { r, a, b -> if (r[a] > r[b]) 1 else 0 }
    "eqir" -> { r, a, b -> if (a == r[b]) 1 else 0 }
    "eqri" -> { r, a, b -> if (r[a] == b) 1 else 0 }
    "eqrr" -> { r, a, b -> if (r[a] == r[b]) 1 else 0 }
    else -> throw IllegalStateException(this)
}