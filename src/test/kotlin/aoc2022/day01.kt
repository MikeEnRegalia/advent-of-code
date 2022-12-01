package aoc2022

import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Test
import strikt.api.expect

class Day01Test {

    @Test
    fun `actual input`() {
        val input = String(IOUtils.resourceToByteArray("/challenges/aoc2022-day01.txt"))
        test(input)
    }

    @Test
    fun `test input`() {
        val input = ""
        test(input)
    }

    private fun test(input: String) {
        val (part1, part2) = day01(input)
        println("part1: $part1")
        println("part2: $part2")
        expect {
        }
    }
}