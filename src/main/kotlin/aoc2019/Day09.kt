package aoc2019

fun main() {
    val program = readln().split(",").map(String::toLong)
    runIntCode(program, input = { 1 })
    runIntCode(program, input = { 2 })
}