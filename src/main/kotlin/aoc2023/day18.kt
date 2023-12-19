import State.*

fun main() {

    data class Trench(val x: Long, val y: Long, val height: Long) {
        val yRange = y..y + height
    }

    fun solve(instructions: List<Pair<String, Long>>): Long {
        val trenches = buildSet {
            var (x, y) = 0L to 0L
            for ((direction, n) in instructions) when (direction) {
                "D" -> y += n.also { add(Trench(x, y, n)) }
                "U" -> y -= n.also { add(Trench(x, y - n, n)) }
                "L", "R" -> x += if (direction == "L") -n else n
            }
        }

        data class CountingState(
            val ranges: List<LongRange> = listOf(),
            val entered: Trench? = null,
            val prev: Trench? = null,
            val mode: State = outside
        )

        return (trenches.minOf { it.y }..trenches.maxOf { it.y + it.height }).sumOf { y ->
            trenches.filter { y in it.yRange }.sortedBy { it.x }.fold(CountingState()) { acc, trench ->
                with(acc) {
                    val (prevIsStart, prevIsEnd) = (y == prev?.yRange?.first) to (y == prev?.yRange?.last)
                    val (isStart, isEnd) = (y == trench.yRange.first) to (y == trench.yRange.last)
                    when {
                        mode == outside && (isStart || isEnd) -> copy(entered = trench, prev = trench, mode = entering)
                        mode == outside -> copy(entered = trench, prev = trench, mode = inside)
                        mode == entering && (prevIsStart && isStart || prevIsEnd && isEnd) -> CountingState(
                            ranges = ranges + listOf(entered!!.x..trench.x),
                            mode = outside
                        )

                        mode == entering -> copy(mode = inside, prev = null)
                        mode == inside && (isStart || isEnd) -> copy(mode = leaving, prev = trench)
                        mode == leaving && (prevIsStart && isStart || prevIsEnd && isEnd) -> copy(
                            mode = inside,
                            prev = null
                        )

                        mode == inside || mode == leaving -> copy(
                            ranges = ranges + listOf(entered!!.x..trench.x),
                            entered = null,
                            mode = outside
                        )

                        else -> throw Exception()
                    }
                }
            }.ranges.sumOf { it.last - it.first + 1 }
        }

    }

    with(generateSequence(::readLine).toList()) {
        println(solve(map { line -> line.split(" ").let { it[0] to it[1].toLong() } }))
        println(solve(map { line ->
            line.substringAfter("#").dropLast(1).let {
                "RDLU"[it.last().digitToInt()].toString() to it.dropLast(1).toLong(16)
            }
        }))
    }
}

private enum class State { outside, entering, inside, leaving }