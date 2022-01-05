package aoc2019

fun main() {
    val program = readln().split(",")
    runIntCode(program.map(String::toInt), { 1 }, ::println)
    runIntCode(program.map(String::toInt), { 5 }, ::println)
}