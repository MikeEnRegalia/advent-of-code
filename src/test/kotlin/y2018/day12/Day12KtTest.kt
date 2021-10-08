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
            .toMap()
        val rules = input.substring(input.indexOf("\n\n") + 2)
            .split("\n")
            .map { it.split(Regex(""" => """)) }
            .map { (neighbors, plant) ->
                Pair(neighbors
                    .mapIndexed { index, c -> Pair(index - 2, c == '#') }
                    .toMap(), plant == "#"
                )
            }

        println(initialState)
        println(rules)

        simulateGrowth(initialState, rules) shouldBe 42
    }

}