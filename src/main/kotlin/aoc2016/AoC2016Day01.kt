package aoc2016.day01

import aoc2016.day01.Direction.*
import kotlin.math.abs

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    ;

    fun turn(right: Boolean) = values().getModulo(this, if (right) 1 else -1)
}

fun <T> Array<T>.getModulo(t: T, offset: Int) = this[(indexOf(t) + size + offset) % size]

data class Pos(val x: Int, val y: Int, val facing: Direction) {
    fun execute(s: String): Pos {
        val right = s.substring(0, 1) == "R"
        val steps = s.substring(1).toInt()
        return turn(right).move(steps)
    }

    fun move(steps: Int) = when (facing) {
        NORTH -> copy(y = y - steps)
        SOUTH -> copy(y = y + steps)
        EAST -> copy(x = x + steps)
        WEST -> copy(x = x - steps)
    }

    fun turn(right: Boolean) = copy(facing = facing.turn(right))

}

fun Pos.distance() = abs(x) + abs(y)

fun aoc2016day01Part1(input: String) =
    input.split(", ".toRegex())
        .fold(Pos(0, 0, NORTH), Pos::execute)
        .distance()