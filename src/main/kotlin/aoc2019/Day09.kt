package aoc2019

fun main() {
    val program = readln().split(",")
    runIntCode(program.map(String::toLong), input = { 1 })

}