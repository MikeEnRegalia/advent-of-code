package y2018.day12

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
        val initialState = input.substring("initial state: ".length, input.indexOf("\n\n"))
            .mapIndexedNotNull { index, c -> index.takeIf { c == '#' } }
            .toSet()

        val rules = input.substring(input.indexOf("\n\n") + 2)
            .split("\n")
            .map { it.split(Regex(""" => """)) }
            .map { (neighbors, plant) ->
                Pair(neighbors.mapIndexedNotNull { index, c -> (index - 2).takeIf { c == '#' } }.toSet(), plant == "#")
            }

        initialState.simulateGrowth(rules, 20) shouldBe 2911
        initialState.simulateGrowth(rules, 50_000_000_000L) shouldBe 2500_000_000_695L
    }

}