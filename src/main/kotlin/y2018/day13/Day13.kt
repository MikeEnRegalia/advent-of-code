package y2018.day13

import y2018.day13.Action.*
import y2018.day13.Direction.*

typealias Point = Pair<Int, Int>

fun crash(input: List<String>): Pair<Point?, Point?> {
    val movingCars = mutableListOf<Car?>()
    val map = input.mapIndexed { y, r ->
        r.mapIndexed { x, c -> c.underneathCar().also { c.Car(x, y)?.let { movingCars.add(it) } } }
    }
    movingCars.sortWith(compareBy({ it?.pos?.x }, { it?.pos?.y }))

    var firstCrashAt: Point? = null
    while (movingCars.filterNotNull().size > 1) {
        movingCars.withIndex().forEach { (index, car) ->
            if (car == null) return@forEach
            with(car) {
                copy(pos = pos.to(direction))
                    .run {
                        when (map[pos.y][pos.x]) {
                            '+' -> atIntersection()
                            '/' -> copy(direction = direction.turnAtSlashCorner())
                            '\\' -> copy(direction = direction.turnAtBackslashCorner())
                            else -> this
                        }
                    }.apply {
                        movingCars[index] = this
                        val crashed = movingCars.filter { it?.pos == pos }
                        if (crashed.count() > 1) {
                            if (firstCrashAt == null) firstCrashAt = pos.x to pos.y
                            movingCars.withIndex()
                                .filter { (_, c) -> crashed.contains(c) }
                                .forEach { (i) -> movingCars[i] = null }
                        }
                    }

            }
        }
    }

    val lastCarPos = movingCars.firstNotNullOf { it }.pos.let { Point(it.x, it.y) }

    return Pair(firstCrashAt, lastCarPos)
}

private fun Char.isCar() = this == '>' || this == '<' || this == '^' || this == 'v'

private fun Char.underneathCar() = when (this) {
    '>', '<' -> '-'
    '^', 'v' -> '|'
    else -> this
}

data class Pos(val x: Int, val y: Int) {
    fun to(direction: Direction) = when (direction) {
        UP -> copy(y = y - 1)
        DOWN -> copy(y = y + 1)
        LEFT -> copy(x = x - 1)
        RIGHT -> copy(x = x + 1)
    }
}

fun Char.Car(x: Int, y: Int) = takeIf { it.isCar() }?.let { Car(Pos(x, y), it.asDirection(), TURN_LEFT) }

data class Car(val pos: Pos, val direction: Direction, val nextAction: Action) {
    fun atIntersection() = when (nextAction) {
        TURN_LEFT -> copy(direction = direction.turnedLeft(), nextAction = FORWARD)
        FORWARD -> copy(nextAction = TURN_RIGHT)
        TURN_RIGHT -> copy(direction = direction.turnedRight(), nextAction = TURN_LEFT)
    }
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    fun turnedLeft() = when (this) {
        UP -> LEFT
        DOWN -> RIGHT
        LEFT -> DOWN
        RIGHT -> UP
    }

    fun turnedRight() = when (this) {
        UP -> RIGHT
        DOWN -> LEFT
        LEFT -> UP
        RIGHT -> DOWN
    }

    fun vertical() = this == UP || this == DOWN

    fun turnAtSlashCorner() = if (vertical()) turnedRight() else turnedLeft()
    fun turnAtBackslashCorner() = if (vertical()) turnedLeft() else turnedRight()
}

private fun Char.asDirection() = when (this) {
    '>' -> RIGHT
    '<' -> LEFT
    '^' -> UP
    'v' -> DOWN
    else -> throw IllegalArgumentException(toString())
}

enum class Action {
    TURN_LEFT,
    FORWARD,
    TURN_RIGHT
}

