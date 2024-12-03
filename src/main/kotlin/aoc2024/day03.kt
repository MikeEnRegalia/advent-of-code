package aoc2024

fun main() {
    val code = generateSequence(::readLine).joinToString()

    fun calc(code: String) = code.split("""(mul\(|\))""".toRegex()).asSequence()
        .map { it.split(",") }
        .filter { it.size == 2 }
        .map { it.mapNotNull(String::toIntOrNull) }
        .filter { it.size == 2 }
        .sumOf { it[0] * it[1] }

    println(calc(code))
    println(code.split("""don't\(\).*?(do\(\)|$)""".toRegex()).sumOf(::calc))
}
