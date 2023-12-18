fun main() {

    data class Pos(val x: Long, val y: Long) {
        override fun toString() = "[$x, $y]"
    }

    data class Rectangle(val from: Pos, val to: Pos = from) {
        val xRange = from.x..to.x
        val yRange = from.y..to.y
        val width = to.x - from.x + 1
        val height = to.y - from.y + 1
        val area = width * height
        val corners by lazy { listOf(from, to, Pos(from.x, to.y), Pos(to.x, from.y)) }
        operator fun contains(pos: Pos) = pos.x in xRange && pos.y in yRange
        operator fun contains(r: Rectangle) = r.corners.all { it in this }
        fun growLeft() = copy(from = Pos(from.x - 1, from.y))
        fun growUp() = copy(from = Pos(from.x, from.y - 1))
        fun growRight() = copy(to = Pos(to.x + 1, to.y))
        fun growDown() = copy(to = Pos(to.x, to.y + 1))
        fun overlapsWith(other: Rectangle) = corners.any { it in other } || other.corners.any { it in this }
        fun surrounding(): Sequence<Pos> {
            val X = from.x - 1..to.x + 1
            val Y = from.y - 1..to.y + 1
            return sequenceOf(
                X.asSequence().map { Pos(it, Y.first) },
                X.asSequence().map { Pos(it, Y.last) },
                Y.asSequence().map { Pos(X.first, it) },
                Y.asSequence().map { Pos(X.last, it) },
            ).flatten()
        }

        override fun toString() = "<$from, $to>"
    }

    fun solve(instructions: List<Pair<String, Long>>) {
        var pos = Pos(0, 0)
        val trenches = mutableSetOf(Rectangle(pos))

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
            var grown = this
            while (true) {
                var r = grown
                r.growLeft().takeIf(Rectangle::qualifies)?.also { r = it }
                r.growRight().takeIf(Rectangle::qualifies)?.also { r = it }
                r.growUp().takeIf(Rectangle::qualifies)?.also { r = it }
                r.growDown().takeIf(Rectangle::qualifies)?.also { r = it }
                if (r == grown) return grown
                grown = r
            }
        }

        fun Rectangle.addToOutside() {
            val toFollow = mutableListOf(this)
            while (toFollow.isNotEmpty()) {
                val curr = toFollow.removeLast().takeIf(Rectangle::qualifies)?.grow() ?: continue
                outside += curr
                curr.surrounding().filter { trenches.none { t -> it in t} }.forEach { toFollow += Rectangle(it) }
            }
        }

        sequenceOf(
            SPACE.xRange.asSequence().map { Pos(it, SPACE.yRange.first) },
            SPACE.xRange.asSequence().map { Pos(it, SPACE.yRange.last) },
            SPACE.yRange.asSequence().map { Pos(SPACE.xRange.first, it) },
            SPACE.yRange.asSequence().map { Pos(SPACE.xRange.last, it) },
        ).flatten().map(::Rectangle).forEach(Rectangle::addToOutside)

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