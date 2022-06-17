package aoc2021b

fun main() = runViaStdInOut { day03() }

fun List<String>.day03(): Pair<Int, Int> {
    fun part1(value: Boolean) = first().indices
        .joinToString("") { isMostCommon(it, value).toChar().toString() }
        .toInt(2)

    fun part2(findMostCommon: Boolean) = toMutableList().run {
        for (bit in first().indices) {
            val keep = isMostCommon(bit, true) xor !findMostCommon
            retainAll { it[bit] == keep.toChar() }
            if (size == 1) break
        }
        single().toInt(2)
    }

    return part1(true) * part1(false) to part2(true) * part2(false)
}

private fun List<String>.isMostCommon(bit: Int, value: Boolean) =
    map { it[bit] }.count { it == value.toChar() } >= size / 2.0

private fun Boolean.toChar() = if (this) '1' else '0'