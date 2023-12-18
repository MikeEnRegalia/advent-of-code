fun main() {

    data class Pos(val x: Long, val y: Long) {
        override fun toString() = "[$x, $y]"
    }

    data class Rectangle(val from: Pos, val to: Pos = from) {
        val xRange = from.x..to.x
        val yRange = from.y..to.y
        val corners = listOf(from, to, Pos(from.x, to.y), Pos(to.x, from.y))
        val area = (to.x - from.x + 1) * (to.y - from.y + 1)
        operator fun contains(pos: Pos) = pos.x in xRange && pos.y in yRange
        operator fun contains(r: Rectangle) = r.from in this && r.to in this
        fun growLeft() = copy(from = Pos(from.x - 1, from.y))
        fun growUp() = copy(from = Pos(from.x, from.y - 1))
        fun growRight() = copy(to = Pos(to.x + 1, to.y))
        fun growDown() = copy(to = Pos(to.x, to.y + 1))
        fun overlapsWith(it: Rectangle) = corners.any { c -> c in it } || it.corners.any { c -> c in this }
        fun surrounding(): Sequence<Pos> {
            val X = xRange.first - 1..xRange.last + 1
            val Y = yRange.first - 1..yRange.last + 1
            return X.asSequence().flatMap { x ->
                Y.asSequence().filter { y -> x == X.first || x == X.last || y == Y.first || y == Y.last }
                    .map { y -> Pos(x, y) }
            }
        }

        override fun toString() = "<$from, $to>"
    }

    fun solve(instructions: List<Pair<String, Long>>) {
        val trenches = mutableSetOf<Rectangle>()

        var pos = Pos(0, 0)
        trenches += Rectangle(Pos(0, 0), Pos(0, 0))

        instructions.forEach { (direction, n) ->
            val (trench, newPos) = when (direction) {
                "U" -> Rectangle(Pos(pos.x, pos.y - n), Pos(pos.x, pos.y - 1)) to pos.copy(y = pos.y - n)
                "D" -> Rectangle(Pos(pos.x, pos.y + 1), Pos(pos.x, pos.y + n)) to pos.copy(y = pos.y + n)
                "R" -> Rectangle(Pos(pos.x + 1, pos.y), Pos(pos.x + n, pos.y)) to pos.copy(x = pos.x + n)
                "L" -> Rectangle(Pos(pos.x - n, pos.y), Pos(pos.x - 1, pos.y)) to pos.copy(x = pos.x - n)
                else -> throw Exception()
            }
            trenches += trench
            pos = newPos
        }

        val SPACE = Rectangle(
            Pos(trenches.minOf { it.from.x }, trenches.minOf { it.from.y }),
            Pos(trenches.maxOf { it.to.x }, trenches.maxOf { it.to.y })
        )

        val outside = mutableSetOf<Rectangle>()

        fun Rectangle.qualifies() =
            this in SPACE && trenches.none { overlapsWith(it) } && outside.none { overlapsWith(it) }

        fun Rectangle.grow(): Rectangle {
            if (!qualifies()) throw Exception()
            var grown = this
            while (true) {
                var r = grown
                r.growLeft().takeIf(Rectangle::qualifies)?.also { r = it }
                r.growRight().takeIf(Rectangle::qualifies)?.also { r = it }
                r.growUp().takeIf(Rectangle::qualifies)?.also { r = it }
                r.growDown().takeIf(Rectangle::qualifies)?.also { r = it }
                if (r == grown) {
                    if (!grown.qualifies()) throw Exception()
                    return grown
                }
                grown = r
            }
        }

        SPACE.xRange.asSequence().flatMap { x ->
            SPACE.yRange.asSequence()
                .filter { y -> x == SPACE.xRange.first || x == SPACE.xRange.last || y == SPACE.yRange.first || y == SPACE.yRange.last }
                .map { y -> Pos(x, y) }
        }.map(::Rectangle).forEach { seed ->
            val toFollow = mutableListOf(seed)
            while (toFollow.isNotEmpty()) {
                val curr = toFollow.removeLast().takeIf { it.qualifies() }?.grow() ?: continue
                outside += curr
                curr.surrounding().map { Rectangle(it) }.forEach { toFollow += it }
            }
        }

        for (x in SPACE.xRange) {
            for (y in SPACE.yRange) {
                val o = outside.filter { Pos(x, y) in it }
                if (o.size > 1) throw Exception("$x,$y: $o")
            }
        }
        println(SPACE.area - outside.sumOf(Rectangle::area))
    }

    val instructions = generateSequence(::readLine).toList()
    val part1Instructions = instructions.map { line ->
        line.split(" ").let { it[0] to it[1].toLong() }
    }

    val part2Instructions = instructions.map { line ->
        line.substringAfter("#").dropLast(1).let {
            "RDLU"[it.last().digitToInt()].toString() to it.dropLast(1).toLong(16)
        }
    }

    solve(part1Instructions)
    solve(part2Instructions)

}