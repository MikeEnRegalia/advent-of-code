package aoc2018.day12

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class Day12KtTest {

    @Test
    fun test() {
        val input =
            """initial state: ##.#...#.#.#....###.#.#....##.#...##.##.###..#.##.###..####.#..##..#.##..#.......####.#.#..#....##.#

#.#.# => #
..#.# => .
.#.## => #
.##.. => .
##... => #
##..# => #
#.##. => #
.#..# => #
.#### => .
....# => .
#.... => .
#.### => .
###.# => #
.#.#. => .
#...# => .
.#... => #
##.#. => #
#..## => #
..##. => .
####. => #
.###. => .
##### => .
#.#.. => .
...#. => .
..#.. => .
###.. => #
#..#. => .
.##.# => .
..... => .
##.## => #
..### => #
...## => #"""

        fun CharSequence.mapPots(offset: Int = 0) =
            mapIndexedNotNull { index, c -> (index + offset).takeIf { c == '#' }?.toLong() }.toSet()

        val initialState = input.substring("initial state: ".length, input.indexOf("\n\n")).mapPots()

        val rules = input.substring(input.indexOf("\n\n") + 2).split("\n")
            .map { it.split(Regex(""" => """)) }
            .map { (neighbors, plant) -> neighbors.mapPots(-2) to (plant == "#") }

        with(initialState) {
            simulateGrowth(rules, 20) shouldBe 2911
            simulateGrowth(rules, 50_000_000_000L) shouldBe 2500_000_000_695L
        }
    }

}

