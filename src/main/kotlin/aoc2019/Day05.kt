package aoc2019

fun main() {
    val program = readln().split(",")
    runIntCode(program.map(String::toLong), { 1 }, ::println)
    runIntCode(program.map(String::toLong), { 5 }, ::println)
}