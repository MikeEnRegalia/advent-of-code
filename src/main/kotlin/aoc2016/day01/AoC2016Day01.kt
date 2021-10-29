package aoc2016.day01

import aoc2016.day01.Direction.*
import kotlin.math.abs

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    ;

    fun turn(right: Boolean) = if (right) values().nextWrapping(this) else values().prevWrapping(this)
}

fun <T> Array<T>.nextWrapping(t: T) = getModulo(indexOf(t) + 1)
fun <T> Array<T>.prevWrapping(t: T) = getModulo(indexOf(t) - 1)
fun <T> Array<T>.getModulo(index: Int) = this[(index + size) % size]

data class Pos(val x: Int, val y: Int, val facing: Direction) {
    fun execute(s: String) = turn(s.substring(0, 1) == "R").move(s.substring(1).toInt())

    fun move(steps: Int) = when (facing) {
        NORTH -> copy(y = y - steps)
        SOUTH -> copy(y = y + steps)
        EAST -> copy(x = x + steps)
        WEST -> copy(x = x - steps)
    }

    fun turn(right: Boolean) = copy(facing = facing.turn(right))
}

fun aoc2016day01Part1(input: String) =
    input.split(", ".toRegex())
        .fold(Pos(0, 0, NORTH), Pos::execute)
        .let { (x, y) -> abs(x) + abs(y) }