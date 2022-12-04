package aoc2022

fun main() = day04(String(System.`in`.readAllBytes())).forEach(::println)

fun day04(input: String) = input.lines()
    .map { line ->
        line.split(",").map { elf ->
            elf.split("-").map(String::toInt).let { it[0]..it[1] }
        }
    }
    .run {
        listOf(Iterable<Int>::all, Iterable<Int>::any).map { op ->
            count { (a, b) -> op(a) { it in b } || op(b) { it in a } }
        }
    }
