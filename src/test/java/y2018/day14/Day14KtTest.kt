package y2018.day14

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

internal class Day14KtTest {
    @Test
    fun test() {
        scoreRecipesAfter(9) shouldBe "5158916779"
    }

    @Test
    fun part1() {
        scoreRecipesAfter(293801) shouldBe "3147574107"
    }

    @TestFactory
    fun part2() = listOf(
        "51589" to 9,
        "59414" to 2018,
        "293801" to 20280190
    ).map { (input, result) ->
        dynamicTest(input) {
            findScoreSequence(input.map { it.toString().toInt() }) shouldBe result
        }
    }
}