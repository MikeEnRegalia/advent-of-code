package y2018.day13

import y2018.day13.Action.*
import y2018.day13.Direction.*

fun crash(input: List<String>): Pair<Pair<Int, Int>?, Pair<Int, Int>?> {
    val cars = input.mapIndexed { y, row ->
        row.mapIndexedNotNull { x, c ->
            if (!c.isCar()) null else Car(Pos(x, y), c.toDirection(), TURN_LEFT)
        }
    }.flatten().sortedWith(compareBy({ it.pos.x }, { it.pos.y }))

    val map = input.map { it.map(Char::underneathCar) }

    var tick = 0
    val movingCars = mutableListOf<Car?>().apply { addAll(cars) }
    var firstCrashAt: Pair<Int, Int>? = null
    while (true) {
        if (movingCars.filterNotNull().size == 1) break

        for ((index, car) in movingCars.withIndex()) {
            if (car == null) continue
            with(car) {
                copy(pos = pos.to(direction))
                    .run {
                        when (val tile = map[pos.y][pos.x]) {
                            '+' -> turnAtIntersection()
                            '/', '\\' -> copy(direction = direction.turnAtCorner(tile))
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

    val lastCarPos = movingCars.firstNotNullOf { it }.pos.let { Pair(it.x, it.y) }

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

data class Car(val pos: Pos, val direction: Direction, val nextAction: Action) {
    fun turnAtIntersection() = when (nextAction) {
        TURN_LEFT -> copy(direction = direction.left(), nextAction = FORWARD)
        FORWARD -> copy(nextAction = TURN_RIGHT)
        TURN_RIGHT -> copy(direction = direction.right(), nextAction = TURN_LEFT)
    }
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    fun left() = when (this) {
        UP -> LEFT
        DOWN -> RIGHT
        LEFT -> DOWN
        RIGHT -> UP
    }

    fun right() = when (this) {
        DOWN -> LEFT
        UP -> RIGHT
        RIGHT -> DOWN
        LEFT -> UP
    }

    fun turnAtCorner(tile: Char) = when (this) {
        UP -> if (tile == '/') RIGHT else LEFT
        DOWN -> if (tile == '/') LEFT else RIGHT
        LEFT -> if (tile == '/') DOWN else UP
        RIGHT -> if (tile == '/') UP else DOWN
    }
}

private fun Char.toDirection() = when (this) {
    '>' -> RIGHT
    '<' -> LEFT
    '^' -> UP
    'v' -> DOWN
    else -> throw IllegalArgumentException()
}

enum class Action {
    TURN_LEFT,
    FORWARD,
    TURN_RIGHT
}

