package aoc2022

fun main() {
    val jets = generateSequence(::readlnOrNull).first().split("").filter { it.isNotBlank() }

    data class Pos(val x: Int, val y: Int) {
        operator fun plus(p: Pos) = Pos(x + p.x, y + p.y)
        operator fun minus(p: Pos) = Pos(x - p.x, y - p.y)
    }

    data class Rock(val points: Set<Pos>, val pos: Pos = Pos(0, 0)) {
        fun minX() = points.minOf { it.x }
        fun maxX() = points.maxOf { it.x }
        fun minY() = points.minOf { it.y }
        fun maxY() = points.maxOf { it.y }
        fun at(p: Pos) = copy(pos = p, points = points.map { it - pos + p }.toSet())
        fun left() = at(pos + Pos(-1, 0))
        fun right() = at(pos + Pos(1, 0))
        fun down() = at(pos + Pos(0, -1))
        fun intersects(shape: Rock) = points.intersect(shape.points).isNotEmpty()
        fun similar(shape: Rock) = (points.first().y - shape.points.first().y).let { dy ->
            points.zip(shape.points).all { (a, b) -> b + Pos(0, dy) == a }
        }
    }

    fun Iterable<Rock>.merge() = flatMap { it.points }.toSet().let { points ->
        Rock(points, Pos(points.minOf { it.x }, points.minOf { it.y }))
    }

    val rocks = listOf(
        Rock(setOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(3, 0))),
        Rock(setOf(Pos(0, 1), Pos(1, 1), Pos(2, 1), Pos(1, 0), Pos(1, 2))),
        Rock(setOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(2, 1), Pos(2, 2))),
        Rock(setOf(Pos(0, 0), Pos(0, 1), Pos(0, 2), Pos(0, 3))),
        Rock(setOf(Pos(0, 0), Pos(1, 0), Pos(0, 1), Pos(1, 1)))
    )

    var rocksTick = 0
    fun newRock() = rocks[rocksTick].also { rocksTick = (rocksTick + 1) % rocks.size }

    val xRange = 0..6
    var jetsTick = 0
    fun newJet() = jets[jetsTick].also { jetsTick = (jetsTick + 1) % jets.size }

    val stuckRocks = ArrayDeque<Rock>()
    fun Rock.applyJet() = when (newJet()) {
        "<" -> if (minX() > xRange.first && stuckRocks.none { it.intersects(left()) }) left() else this
        else -> if (maxX() < xRange.last && stuckRocks.none { it.intersects(right()) }) right() else this
    }

    fun Rock.canFall() = down().minY() > 0 && stuckRocks.none { it.intersects(down()) }

    var fallingRock = newRock().at(Pos(2, 4))
    var nStuckRocks = 0

    data class State(val jetsTick: Int, val shapesTick: Int, val stuckRocks: Rock)

    val states = mutableMapOf<Int, State>()
    val history = mutableMapOf<Pair<Int, Int>, Pair<Int, Iterable<Rock>>>()

    var cycleStart: Int? = null

    var part1: Int? = null
    var part2: Long? = null
    while (part1 == null || part2 == null) {
        fallingRock = fallingRock.applyJet()
        if (fallingRock.canFall()) {
            fallingRock = fallingRock.down()
            continue
        }

        stuckRocks += fallingRock
        nStuckRocks++
        states[nStuckRocks] = State(jetsTick, rocksTick, stuckRocks.merge())

        if (stuckRocks.size > 40) {
            stuckRocks.removeFirst()

            val k = Pair(jetsTick, rocksTick)
            val prev = history[k]
            if (prev != null) {
                val (prevRocks, prevShapes) = prev
                val prevShape = prevShapes.merge()
                val currShape = stuckRocks.merge()
                if (prevShape.similar(currShape) && cycleStart == null) {
                    val cycleHeight = currShape.maxY() - prevShape.maxY()
                    val cycleCount = nStuckRocks - prevRocks
                    cycleStart = prevRocks

                    val fullCycles = (1_000_000_000_000 - cycleStart) / cycleCount
                    val partialCycle = ((1_000_000_000_000 - cycleStart) % cycleCount).toInt()
                    val remaining = states.getValue(cycleStart + partialCycle).stuckRocks.maxY() - cycleStart
                    part2 = prevRocks + (fullCycles * cycleHeight) + remaining
                }
            }
            history[k] = nStuckRocks to stuckRocks.toList()
        }

        val currentHeight = stuckRocks.maxOf { it.maxY() }
        if (nStuckRocks == 2022)
            part1 = currentHeight

        fallingRock = newRock().at(Pos(2, currentHeight + 4))
    }

    println(part1)
    println(part2)
}

