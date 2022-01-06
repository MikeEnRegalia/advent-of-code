package aoc2016

fun main() {
    val lines = generateSequence { readLine() }
    val part1 = lines.count { line ->
        val abbas = (0..line.length - 4).filter {
            val abba = line.substring(it, it + 4)
            abba[0] != abba[1] && abba.substring(0, 2) == abba.substring(2).reversed()
        }
        abbas.none { line.inBrackets(it) } && abbas.any { !line.inBrackets(it) }
    }
    println(part1)
}

fun String.inBrackets(pos: Int) =
    (indexOf('[', pos) to indexOf(']', pos)).let { (open, close) -> close != -1 && (open == -1 || close < open) }