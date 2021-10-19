package aoc2018.day17

import aoc2018.day18.day18Settlers
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class AoC12018Day18KtTest {
    @Test
    fun test() {
        day18Settlers(
            """.#.#...|#.
.....#|##|
.|..|...#.
..|#.....#
#.#|||#|#|
...#.||...
.|....|...
||...#|.#|
|.||||..|.
...#.|..|."""
        ) shouldBe 1147
    }
}