package aoc2021b

fun main() = Day24(generateSequence(::readLine).toList()).monad().let(::println)

class Day24(input: List<String>) {
    private val program = input.map { it.split(" ") }

    fun monad(data: List<Int> = listOf(), r: List<Int> = listOf(0, 0, 0, 0), pos: Int = 0): Pair<Long, Long>? =
        when (data.size) {
            14 -> data.takeIf { r.last() == 0 }?.joinToString("", transform = Int::toString)?.toLong()?.let { it to it }
            else -> digitsToTry(data, r)
                .mapNotNull { monadDigit(it, pos, r).let { (pos, r) -> monad(data.plus(it), r, pos) } }
                .takeIf { it.isNotEmpty() }
                ?.let { list -> list.minOf { it.first } to list.maxOf { it.second } }
        }

    // I only know my input, so no idea whether this works for other users :-)
    private val monadReductions = input.windowed(2).filter { it[0].startsWith("div z ") }.map {
        if (it[0].endsWith(" 1")) null else it[1].substringAfterLast(" ").toInt()
    }

    private fun digitsToTry(data: List<Int>, r0: List<Int>) = when (val offset = monadReductions[data.size]) {
        null -> 1..9
        else -> ((r0.last() % 26) + offset).let { if (it !in 1..9) emptyList() else listOf(it) }
    }

    private fun monadDigit(digit: Int, pos: Int = 0, r0: List<Int>): Pair<Int, List<Int>> {
        val r = r0.toMutableList()
        val rn = listOf("w", "x", "y", "z")
        var secondInput = false
        program.drop(pos).forEachIndexed { programLine, instructions ->
            val (cmd, a) = instructions
            fun b() = instructions.last().let { if (it in rn) r[rn.indexOf(it)] else it.toInt() }
            val aI = rn.indexOf(a)
            when (cmd) {
                "inp" -> if (secondInput) return pos + programLine to r else {
                    r[0] = digit
                    secondInput = true
                }

                "add" -> r[aI] = r[aI] + b()
                "mul" -> r[aI] = r[aI] * b()
                "div" -> r[aI] = r[aI] / b()
                "mod" -> r[aI] = r[aI] % b()
                "eql" -> r[aI] = if (r[aI] == b()) 1 else 0
            }
        }
        return Pair(program.size, r)
    }
}