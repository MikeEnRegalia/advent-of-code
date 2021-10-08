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
            .mapIndexed { index, c -> Pair(index, c == '#') }
            .filter { it.second }
            .map { it.first }
            .toSet()
        val rules = input.substring(input.indexOf("\n\n") + 2)
            .split("\n")
            .map { it.split(Regex(""" => """)) }
            .map { (neighbors, plant) ->
                Pair(neighbors
                    .mapIndexed { index, c -> Pair(index - 2, c == '#') }
                    .toMap(), plant == "#"
                )
            }

        initialState.simulateGrowth(rules, 20) shouldBe 2911
        initialState.simulateGrowth(rules, 50_000_000_000L) shouldBe 2500_000_000_695L
    }

}