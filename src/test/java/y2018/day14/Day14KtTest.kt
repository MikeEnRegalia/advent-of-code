package y2018.day14

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

internal class Day14KtTest {

    @TestFactory
    fun part1() = listOf(9 to "5158916779", 293801 to "3147574107").map { (input, expected) ->
        dynamicTest("$input -> $expected") { scoreRecipesAfter(input) shouldBe expected }
    }

    @TestFactory
    fun part2() = listOf(
        "51589" to 9,
        "59414" to 2018,
        "293801" to 20280190
    ).map { (input, expected) ->
        dynamicTest("$input -> $expected") { findScoreSequence(input.map { it.digitToInt() }) shouldBe expected }
    }

}