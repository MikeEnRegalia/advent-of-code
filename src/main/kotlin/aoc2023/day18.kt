import Tracker.State.*

fun main() {
    with(generateSequence(::readLine).toList()) {
        println(solve(map { line -> line.split(" ").let { it[0] to it[1].toLong() } }))
        println(solve(map { line ->
            line.substringAfter("#").dropLast(1).let {
                "RDLU"[it.last().digitToInt()].toString() to it.dropLast(1).toLong(16)
            }
        }))
    }
}

private data class Trench(val x: Long, val y: Long, val height: Long) {
    val yRange = y..y + height
}

private data class Tracker(
    val ranges: List<LongRange> = listOf(),
    val entered: Trench? = null,
    val prev: Trench? = null,
    val s: State = outside
) {
    enum class State { outside, entering, inside, leaving }

    fun count(y: Long, trench: Trench): Tracker {
        val (prevIsStart, prevIsEnd) = (y == prev?.yRange?.first) to (y == prev?.yRange?.last)
        val (isStart, isEnd) = (y == trench.yRange.first) to (y == trench.yRange.last)
        return when {
            s == outside && (isStart || isEnd) -> copy(entered = trench, prev = trench, s = entering)
            s == outside -> copy(entered = trench, prev = trench, s = inside)
            s == entering && (prevIsStart && isStart || prevIsEnd && isEnd) -> Tracker(
                ranges = ranges + listOf(entered!!.x..trench.x),
                s = outside
            )

            s == entering -> copy(s = inside, prev = null)
            s == inside && (isStart || isEnd) -> copy(s = leaving, prev = trench)
            s == leaving && (prevIsStart && isStart || prevIsEnd && isEnd) -> copy(
                s = inside,
                prev = null
            )

            s == inside || s == leaving -> copy(
                ranges = ranges + listOf(entered!!.x..trench.x),
                entered = null,
                s = outside
            )

            else -> throw Exception()
        }
    }
}

fun solve(instructions: List<Pair<String, Long>>): Long {
    val trenches = buildSet {
        var (x, y) = 0L to 0L
        for ((direction, n) in instructions) when (direction) {
            "D" -> y += n.also { add(Trench(x, y, n)) }
            "U" -> y -= n.also { add(Trench(x, y - n, n)) }
            "L", "R" -> x += if (direction == "L") -n else n
        }
    }.sortedBy { it.x }

    return with(trenches) {
        (minOf { it.y }..maxOf { it.y + it.height }).sumOf { y ->
            filter { y in it.yRange }.fold(Tracker()) { t, n -> t.count(y, n) }.ranges.sumOf { it.last - it.first + 1 }
        }
    }
}