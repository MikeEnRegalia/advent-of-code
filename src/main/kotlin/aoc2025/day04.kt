package aoc2025

fun main() = generateSequence(::readLine)
    .flatMapIndexed { y, line -> line.mapIndexedNotNull { x, char -> if (char == '@') x to y else null } }
    .toMutableSet().let { grid ->
        generateSequence {
            grid.filter { (px, py) ->
                (-1..1).flatMap { y -> (-1..1).map { px + it to py + y } }.count { it in grid } <= 4
            }.toSet().also { grid -= it }.size.takeIf { it > 0 }
        }.toList().run {
            println(first())
            println(sum())
        }
    }
