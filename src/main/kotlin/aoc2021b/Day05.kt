package aoc2021b

fun main() = generateSequence(::readLine).toList().day05().let { (p1, p2) -> println(p1); println(p2) }

fun List<String>.day05() = map { it.replace(" -> ", ",").split(",").map(String::toInt) }
    .let { all -> listOf(all.filter { (x1, y1, x2, y2) -> x1 == x2 || y1 == y2 }, all) }
    .map { lines -> lines.points().groupingBy { it }.eachCount().count { it.value > 1 } }

private fun List<List<Int>>.points() = flatMap { (x1, y1, x2, y2) ->
    generateSequence(x1 to y1) { (x, y) -> if (x to y == x2 to y2) null else x + x2.compareTo(x) to y + y2.compareTo(y) }
}