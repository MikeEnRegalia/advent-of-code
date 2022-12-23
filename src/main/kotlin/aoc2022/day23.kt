package aoc2022

fun main() {
    data class Pos(val x: Int, val y: Int) {
        fun surrounding() = (-1..1).flatMap {
                dy -> (-1..1).map { dx -> Pos(x + dx, y + dy) }
        }.filter { it != this }

        fun facing(d: Int) = when (d) {
            0 -> (-1..1).map { dx -> Pos(x + dx, y - 1) }
            1 -> (-1..1).map { dx -> Pos(x + dx, y + 1) }
            2 -> (-1..1).map { dy -> Pos(x - 1, y + dy) }
            else -> (-1..1).map { dy -> Pos(x + 1, y + dy) }
        }
    }

    val elves = generateSequence(::readlnOrNull)
        .flatMapIndexed { y, line -> line.mapIndexedNotNull { x, c -> if (c == '#') Pos(x, y) else null } }
        .toMutableSet()

    var dir = 0
    var round = 0
    while(true) {
        val proposals = mutableMapOf<Pos, MutableList<Pos>>()
        for (elf in elves) {
            if (elf.surrounding().none { it in elves }) continue

            for (d in dir until dir + 4) {
                val facing = elf.facing(d % 4)
                if (facing.none { it in elves }) {
                    proposals.compute(facing[1]) { _, old -> (old ?: mutableListOf()).apply { add(elf) } }
                    break
                }
            }
        }
        var movedElves = 0
        for ((to, candidates) in proposals.filter { it.value.size == 1}) {
            elves -= candidates.single()
            elves += to
            movedElves++
        }

        dir = (dir + 1) % 4
        round++

        if (round == 10) {
            val (minX, maxX) = elves.minOf { it.x } to elves.maxOf { it.x }
            val (minY, maxY) = elves.minOf { it.y } to elves.maxOf { it.y }
            val totalArea = (1 + maxX - minX) * (1 + maxY - minY)
            println(totalArea - elves.size)
        }

        if (movedElves == 0) {
            println(round)
            break
        }
    }
}

