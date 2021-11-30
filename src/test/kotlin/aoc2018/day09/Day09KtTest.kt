package aoc2018.day09

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

internal class Day09KtTest {
    @TestFactory
    fun testDay09() = listOf(
        dynamicTest("test") { day09(9, 25) shouldBe 32 },
        dynamicTest("part1") { day09(468, 71010) shouldBe 374287 },
        dynamicTest("part2") { day09(468, 71010 * 100) shouldBe 3083412635L }
    )
}