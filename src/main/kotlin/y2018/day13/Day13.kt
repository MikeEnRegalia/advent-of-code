package y2018.day13

import y2018.day13.Action.*
import y2018.day13.Direction.*

fun crash(input: List<String>) = with(mutableListOf<Cart>()) {
    val map = input.mapIndexed { y, r -> r.mapIndexed { x, c -> c.asCart(x, y)?.let { add(it); it.track() } ?: c } }

    var firstCrashed: Cart? = null
    while (size > 1) {
        sortWith(compareBy({ it.pos.x }, { it.pos.y }))

        withIndex().forEach { (index, carFromPreviousTick) ->
            carFromPreviousTick.drive(map).let { cart ->
                this[index] = cart
                val crashed = filter { it.pos == cart.pos }
                if (crashed.count() > 1) {
                    if (firstCrashed == null) firstCrashed = cart
                    withIndex()
                        .filter { (_, cart) -> crashed.contains(cart) }
                        .forEach { (i, cart) -> this[i] = cart.crashed() }
                }
            }
        }

        removeIf { it.crashed }
    }

    Pair(firstCrashed?.pos, first().pos)
}

private fun Char.isCart() = this == '>' || this == '<' || this == '^' || this == 'v'

data class Pos(val x: Int, val y: Int) {
    fun to(direction: Direction) = when (direction) {
        UP -> copy(y = y - 1)
        DOWN -> copy(y = y + 1)
        LEFT -> copy(x = x - 1)
        RIGHT -> copy(x = x + 1)
    }
}

internal fun Char.asCart(x: Int, y: Int) = takeIf { it.isCart() }?.let { Cart(Pos(x, y), it.asDirection(), TURN_LEFT) }

internal data class Cart(val pos: Pos, val direction: Direction, val nextAction: Action, val crashed: Boolean = false) {
    fun track() = if (direction.vertical()) '|' else '-'

    fun drive(map: List<List<Char>>) = copy(pos = pos.to(direction)).run {
        when (map[pos.y][pos.x]) {
            '+' -> atIntersection()
            '/' -> copy(direction = direction.turnAtSlashCorner())
            '\\' -> copy(direction = direction.turnAtBackslashCorner())
            else -> this
        }
    }

    private fun atIntersection() = when (nextAction) {
        TURN_LEFT -> copy(direction = direction.turnedLeft(), nextAction = FORWARD)
        FORWARD -> copy(nextAction = TURN_RIGHT)
        TURN_RIGHT -> copy(direction = direction.turnedRight(), nextAction = TURN_LEFT)
    }

    fun crashed() = copy(crashed = true)
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

