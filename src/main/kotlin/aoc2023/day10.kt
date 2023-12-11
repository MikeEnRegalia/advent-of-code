package aoc2023

import kotlin.math.min

fun main() {
    val lines = generateSequence(::readlnOrNull).toList()

    data class Pos(val x: Int, val y: Int) {
        val inGrid by lazy { x in lines[0].indices && y in lines.indices }
        val outsideGrid by lazy { neighbors().count() <= 1 }

        val c: Char by lazy {
            when {
                !inGrid -> 'X'
                lines[y][x] == 'S' -> {
                    val (e, s, w, n) = listOf(e, s, w, n).map { it.c }
                    when {
                        e in connectsFromWest && w in connectsFromEast -> '-'
                        s in connectsFromNorth && n in connectsFromSouth -> '|'
                        e in connectsFromWest && s in connectsFromSouth -> 'F'
                        e in connectsFromWest && n in connectsFromNorth -> 'L'
                        w in connectsFromEast && s in connectsFromSouth -> '7'
                        w in connectsFromEast && n in connectsFromNorth -> 'J'
                        else -> '?'
                    }
                }

                else -> lines[y][x]
            }
        }

        val w by lazy { translate(dx = -1) }
        val e by lazy { translate(dx = 1) }
        val n by lazy { translate(dy = -1) }
        val s by lazy { translate(dy = 1) }

        val nw by lazy { translate(-1, -1) }
        val ne by lazy { translate(1, -1) }
        val sw by lazy { translate(-1, 1) }
        val se by lazy { translate(1, 1) }

        private fun translate(dx: Int = 0, dy: Int = 0) = copy(x = x + dx, y = y + dy)

        fun neighbors() = sequenceOf(w, e, n, s).filter { it.inGrid }

        fun pipeNeighbors() = neighbors().filter {
            val dx = it.x - x
            val dy = it.y - y
            when {
                dx == 1 -> c in connectsFromEast && it.c in connectsFromWest
                dx == -1 -> c in connectsFromWest && it.c in connectsFromEast
                dy == 1 -> c in connectsFromSouth && it.c in connectsFromNorth
                dy == -1 -> c in connectsFromNorth && it.c in connectsFromSouth
                else -> false
            }
        }
    }

    fun findPath(): Pair<List<Pos>, Int> {
        val start = lines.indexOfFirst { 'S' in it }.let { y -> Pos(lines[y].indexOf('S'), y) }
        val D = mutableMapOf(start to 0)
        val V = mutableSetOf(start)

        var curr = start
        while (true) {
            curr.pipeNeighbors().filter { it !in V }.forEach {
                val d = D.getValue(curr) + 1
                D.compute(it) { _, old -> min(old ?: Int.MAX_VALUE, d) }
            }
            V += curr
            val remaining = D.keys - V
            curr = remaining.minByOrNull { D.getValue(it) } ?: break
        }

        val path = mutableListOf(start)
        while (true) path += path.last().pipeNeighbors().filter { it !in path }.firstOrNull() ?: break

        return path to D.values.max()
    }
    val (path, part1) = findPath()
    fun Pos.isTile() = c != '?' && this !in path
    val leftTiles = mutableSetOf<Pos>()
    val rightTiles = mutableSetOf<Pos>()
    for ((prev, curr, next) in path.plus<Pos>(path.take<Pos>(2)).windowed<Pos>(3)) {

        fun List<Pos>.addTiles(set: MutableSet<Pos>) = filter(Pos::isTile).forEach { set.add(it) }

        val rightIfEastward = if (next.x > prev.x) rightTiles else leftTiles
        val leftIfEastward = if (next.x > prev.x) leftTiles else rightTiles

        with(curr) {
            when (c) {
                'F' -> {
                    listOf(se).addTiles(rightIfEastward)
                    listOf(w, nw, n).addTiles(leftIfEastward)
                }

                '7' -> {
                    listOf(sw).addTiles(rightIfEastward)
                    listOf(n, ne, e).addTiles(leftIfEastward)
                }

                'J' -> {
                    listOf(nw).addTiles(leftIfEastward)
                    listOf(e, se, s).addTiles(rightIfEastward)
                }

                'L' -> {
                    listOf(ne).addTiles(leftIfEastward)
                    listOf(w, sw, s).addTiles(rightIfEastward)
                }
            }
        }
    }
    fun Set<Pos>.exploreTiles(): Set<Pos> {
        val found = toMutableSet()
        val followed = mutableSetOf<Pos>()
        while (true) {
            val pos = found.firstOrNull { it !in followed } ?: return found
            pos.neighbors().filter(Pos::isTile).forEach { found += it }
            followed += pos
        }
    }

    val part2 = listOf(leftTiles, rightTiles)
        .map(MutableSet<Pos>::exploreTiles)
        .single { it.none(Pos::outsideGrid) }
        .size

    listOf(part1, part2).forEach(::println)
}

private const val connectsFromWest = "7-J"
private const val connectsFromEast = "F-L"
private const val connectsFromNorth = "L|J"
private const val connectsFromSouth = "F|7"