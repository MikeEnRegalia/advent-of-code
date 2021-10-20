package aoc2018.day22

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class AoC2018Day22KtTest {
    @Test
    fun test() {
        day22(510, 10, 10) shouldBe 114
    }

    @Test
    fun part1() {
        day22(6969, 9, 796) shouldBe 7901
    }

}