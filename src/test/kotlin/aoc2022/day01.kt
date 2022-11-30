package aoc2022

import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Test

class Day01Test {

    @Test
    fun `test actual solution`() {
        val input = String(IOUtils.resourceToByteArray("/challenges/aoc2022-day01.txt"))
        println(input)
    }
}