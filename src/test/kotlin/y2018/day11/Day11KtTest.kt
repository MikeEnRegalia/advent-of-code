package y2018.day11

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class Day11KtTest {

    @Test
    fun testPower() {
        power(122, 79, 57) shouldBe -5
        power(217, 196, 39) shouldBe 0
        power(101, 153, 71) shouldBe 4
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
        day11(9810, anySquare = true) shouldBe listOf(235, 206, 13)
    }
}

