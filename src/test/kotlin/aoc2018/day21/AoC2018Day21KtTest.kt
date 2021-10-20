package aoc2018.day21

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private const val INPUT = """#ip 2
seti 123 0 5
bani 5 456 5
eqri 5 72 5
addr 5 2 2
seti 0 0 2
seti 0 9 5
bori 5 65536 3
seti 7586220 4 5
bani 3 255 1
addr 5 1 5
bani 5 16777215 5
muli 5 65899 5
bani 5 16777215 5
gtir 256 3 1
addr 1 2 2
addi 2 1 2
seti 27 9 2
seti 0 9 1
addi 1 1 4
muli 4 256 4
gtrr 4 3 4
addr 4 2 2
addi 2 1 2
seti 25 4 2
addi 1 1 1
seti 17 2 2
setr 1 6 3
seti 7 8 2
eqrr 5 0 1
addr 1 2 2
seti 5 0 2"""

internal class AoC2018Day21KtTest {
    @Test
    fun part1() {
        day21(INPUT) shouldBe 11050031
    }

    @Test
    fun part2() {
        day21(INPUT, part2 = true) shouldBe 11341721
    }

}