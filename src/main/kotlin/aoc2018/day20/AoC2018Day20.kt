package aoc2018.day20

data class Room(val x: Int, val y: Int)

class Parser(val input: String) {
    private var inputPos: Int = 0
    private var room = Room(0, 0)
    val doors = mutableMapOf<Room, MutableSet<Room>>()

    fun traverseGroup() {
        next()
        val initialRoom = room
        while (true) {
            when (peek()) {
                '(' -> traverseGroup()
                'N', 'S', 'E', 'W' -> advance()
                '|' -> {
                    room = initialRoom
                    next()
                }
                ')' -> {
                    next()
                    room = initialRoom
                    break
                }
            }
        }
    }

    private fun advance() {
        val nextRoom = when (val next = next()) {
            'N' -> room.copy(y = room.y - 1)
            'S' -> room.copy(y = room.y + 1)
            'W' -> room.copy(x = room.x - 1)
            'E' -> room.copy(x = room.x + 1)
            else -> throw IllegalArgumentException(next.toString())
        }

        doors.getOrPut(room) { mutableSetOf() }.add(nextRoom)
        doors.getOrPut(nextRoom) { mutableSetOf() }.add(room)

        room = nextRoom
    }

    fun peek() = input.getOrNull(inputPos)
    fun next() = peek().also { inputPos++ }
}

fun main() {
    val input = readln().substringAfter("^").substringBefore("$")
    val doors = Parser(input).traverseGroup()
    println(doors)
}