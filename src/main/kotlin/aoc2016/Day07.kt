package aoc2016

fun main() {
    val lines = generateSequence { readLine() }.toList()
    val part1 = lines.count { line ->
        val abbas = (0..line.length - 4).filter {
            val abba = line.substring(it, it + 4)
            abba[0] != abba[1] && abba.substring(0, 2) == abba.substring(2).reversed()
        }
        abbas.none { line.inBrackets(it) } && abbas.any { !line.inBrackets(it) }
    }
    println(part1)
    val part2 = lines.count { line ->
        val abas = (0..line.length - 3).filter {
            val aba = line.substring(it, it + 3)
            aba[0] != aba[1] && aba.substring(0, 2) == aba.substring(1).reversed()
        }
        abas.filter { !line.inBrackets(it) }.any { abaI ->
            val correspondingAba = "${line[abaI + 1]}${line[abaI]}${line[abaI + 1]}"
            abas.filter { line.inBrackets(it) }.any { cabaI -> line.substring(cabaI, cabaI + 3) == correspondingAba }
        }
    }
    println(part2)
}

fun String.inBrackets(pos: Int) =
    (indexOf('[', pos) to indexOf(']', pos)).let { (open, close) -> close != -1 && (open == -1 || close < open) }