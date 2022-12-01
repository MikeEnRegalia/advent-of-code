package aoc2022

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day01Test {

    @Test
    fun `test input`() {
        val input = """
            1000
            2000
            3000
            
            4000
            
            5000
            6000
            
            7000
            8000
            9000
            
            10000
            """.trimIndent()
        val (part1, part2) = day01(input)
        expectThat(part1) { isEqualTo(24000) }
        expectThat(part2) { isEqualTo(45000) }
    }

}