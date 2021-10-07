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
        day11(18) shouldBe (33 to 45)
        day11(42) shouldBe (21 to 61)
    }

    @Test
    fun part1() {
        day11(9810) shouldBe 245 to 14
    }
}

