package aoc2022

fun main() = day04(String(System.`in`.readAllBytes())).forEach(::println)

fun day04(input: String) = input.lines()
    .map { line ->
        line.split(",").map { elf ->
            elf.split("-").map(String::toInt).let { it[0]..it[1] }
        }
    }
    .run {
        listOf(
            count { (a, b) -> a.all { it in b } || b.all { it in a } },
            count { (elf1, elf2) -> elf1.any { it in elf2 } || elf2.any { it in elf1 } })
    }
