package aoc2022

import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.isNull

class Day01Test {

    @Test
    fun `actual input`() {
        val input = String(IOUtils.resourceToByteArray("/challenges/aoc2022-day01.txt")).split("\n")
        test(input)
    }

    @Test
    fun `test input`() {
        val input = listOf("")
        test(input)
    }

    private fun test(input: List<String>) {
        val (part1, part2) = day01(input)
        println("part1: $part1")
        println("part2: $part2")
        expect {
            that(part1).isNull()
            that(part2).isNull()
        }
    }
}