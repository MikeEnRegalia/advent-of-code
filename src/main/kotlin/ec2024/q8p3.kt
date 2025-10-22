package ec2024

import kotlin.math.min

fun main() {
    val priests = readln().toInt()
    val acolytes = 10
    val material = 202400000

    var thickness = 1
    val columns = mutableListOf(1L)
    fun width() = 2 * columns.size - 1L

    var prevBlocks = 1L

    while (true) {
        thickness = acolytes + ((thickness * priests) % acolytes)

        columns += 0
        columns.indices.forEach { i -> columns[i] += thickness }

        val newTotalBlocks = columns[0] + 2 * columns.drop(1).sumOf { it }

        fun Int.removedBlocks() = when (columns.lastIndex) {
            this -> 0
            else -> min(columns[this + 1] - 1, ((priests * width()) * columns[this]) % acolytes)
        }

        val newRemovedBlocks = 0.removedBlocks() + 2 * columns.indices.drop(1).sumOf { it.removedBlocks() }
        val newBlocks = newTotalBlocks - newRemovedBlocks

        if (material in prevBlocks..<newBlocks) {
            println(newBlocks - material)
            break
        }

        prevBlocks = newBlocks
    }
}
