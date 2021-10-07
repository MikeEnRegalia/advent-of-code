package y2018.day11

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class Day11KtTest {

    @Test
    fun testPower() {
        Cell(122, 79).power(57) shouldBe -5
        Cell(217, 196).power(39) shouldBe 0
        Cell(101, 153).power(71) shouldBe 4
    }

    @Test
    fun test() {
        day11(18) shouldBe listOf(33, 45, 3)
        day11(42) shouldBe listOf(21, 61, 3)
    }

    @Test
    fun part1() {
        day11(9810) shouldBe listOf(245, 14, 3)
    }
    @Test
    fun part2() {
        day11(9810, anySquare = true) shouldBe listOf(42, 42, 42)
    }
}

