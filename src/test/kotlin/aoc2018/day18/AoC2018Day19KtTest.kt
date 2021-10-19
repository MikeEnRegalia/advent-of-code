package aoc2018.day18

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private const val INPUT = """#ip 5
addi 5 16 5
seti 1 8 3
seti 1 1 1
mulr 3 1 4
eqrr 4 2 4
addr 4 5 5
addi 5 1 5
addr 3 0 0
addi 1 1 1
gtrr 1 2 4
addr 5 4 5
seti 2 7 5
addi 3 1 3
gtrr 3 2 4
addr 4 5 5
seti 1 5 5
mulr 5 5 5
addi 2 2 2
mulr 2 2 2
mulr 5 2 2
muli 2 11 2
addi 4 8 4
mulr 4 5 4
addi 4 20 4
addr 2 4 2
addr 5 0 5
seti 0 4 5
setr 5 8 4
mulr 4 5 4
addr 5 4 4
mulr 5 4 4
muli 4 14 4
mulr 4 5 4
addr 2 4 2
seti 0 7 0
seti 0 9 5"""

internal class AoC2018Day19KtTest {
    @Test
    fun test() = day19(
        """#ip 0
seti 5 0 1
seti 6 0 2
addi 0 1 0
addr 1 2 3
setr 1 0 0
seti 8 0 4
seti 9 0 5"""
    ) shouldBe 6

    @Test
    fun part1() {
        day19(INPUT) shouldBe 2640
    }

    @Test
    fun part2() {
        day19(INPUT, 1) shouldBe 27024480
    }
}