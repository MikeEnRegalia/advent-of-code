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
            count { (a, b) -> a.any { it in b } || b.any { it in a } })
    }
