package aoc2021b

data class Dot(val x: Int, val y: Int)
data class Instruction(val alongX: Boolean, val at: Int)

fun Set<Dot>.fold(alongX: Boolean, at: Int) = fold(mutableSetOf<Dot>()) { acc, dot ->
    acc.apply {
        val nop = (if (alongX) dot.x else dot.y) < at
        acc.add(if (nop) dot else if (alongX) dot.copy(x = at * 2 - dot.x) else dot.copy(y = at * 2 - dot.y))
    }
}

fun Sequence<String>.readDots() = map { line -> line.split(",").map(String::toInt).let { Dot(it[0], it[1]) } }.toSet()
val paper = generateSequence { readLine().takeIf { it != "" } }.readDots()

var part1: Int? = null
val part2 = generateSequence(::readLine)
    .map { it.split("=").let { (a, b) -> Instruction(a.endsWith("x"), b.toInt()) } }
    .foldIndexed(paper) { i, paper, (alongX, at) -> paper.fold(alongX, at).also { if (i == 0) part1 = it.size } }
    .let { dots ->
        (0..dots.maxOf { it.y }).joinToString("\n") { y ->
            (0..dots.maxOf { it.x }).joinToString("") { x -> if (Dot(x, y) in dots) "#" else " " }
        }
    }

println(part1)
println(part2)