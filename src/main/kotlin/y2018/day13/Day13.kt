package y2018.day13

import y2018.day13.Action.*
import y2018.day13.Direction.*

typealias Point = Pair<Int, Int>

fun crash(input: List<String>): Pair<Point?, Point?> {
    val cars = input.mapIndexed { y, row -> row.mapIndexedNotNull { x, c -> c.Car(x, y) } }
        .flatten()
        .sortedWith(compareBy({ it.pos.x }, { it.pos.y }))

    val map = input.map { it.map(Char::underneathCar) }

    var tick = 0
    val movingCars = mutableListOf<Car?>().apply { addAll(cars) }
    var firstCrashAt: Point? = null
    while (true) {
        if (movingCars.filterNotNull().size == 1) break

        for ((index, car) in movingCars.withIndex()) {
            if (car == null) continue
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
        tick++
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
