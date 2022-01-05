package aoc2019

fun main() {
    runIntCode(readln().split(",").map(String::toInt), { 1 }, ::println)
}