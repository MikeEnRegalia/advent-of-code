package aoc2018.day18


fun day19(input: String, r0: Int = 0): Int {
    val ipRegister = input.substring("#ip ".length, input.indexOf("\n")).toInt()
    val program = input.substring(input.indexOf("\n") + 1).split("\n")
        .map { it.split(" ") }
        .map { row -> row[0].opcode() to row.drop(1).map { it.toInt() } }

    if (r0 == 1) return 10551432.let { n -> (1..n).filter { n % it == 0 }.sum() }

    var ip = 0
    val r = mutableListOf(r0, 0, 0, 0, 0, 0)
    while (ip in program.indices) {
        val (opcode, data) = program[ip]
        r[ipRegister] = ip
        r[data[2]] = r.opcode(data[0], data[1])
        ip = r[ipRegister] + 1
    }
    return r[0]
}

internal typealias Opcode = MutableList<Int>.(Int, Int) -> Int

internal fun String.opcode(): Opcode = when (this) {
    "addr" -> { a, b -> this[a] + this[b] }
    "addi" -> { a, b -> this[a] + b }
    "mulr" -> { a, b -> this[a] * this[b] }
    "muli" -> { a, b -> this[a] * b }
    "banr" -> { a, b -> this[a].and(this[b]) }
    "bani" -> { a, b -> this[a].and(b) }
    "borr" -> { a, b -> this[a].or(this[b]) }
    "bori" -> { a, b -> this[a].or(b) }
    "setr" -> { a, _ -> this[a] }
    "seti" -> { a, _ -> a }
    "gtir" -> { a, b -> if (a > this[b]) 1 else 0 }
    "gtri" -> { a, b -> if (this[a] > b) 1 else 0 }
    "gtrr" -> { a, b -> if (this[a] > this[b]) 1 else 0 }
    "eqir" -> { a, b -> if (a == this[b]) 1 else 0 }
    "eqri" -> { a, b -> if (this[a] == b) 1 else 0 }
    "eqrr" -> { a, b -> if (this[a] == this[b]) 1 else 0 }
    else -> throw IllegalStateException(this)
}
