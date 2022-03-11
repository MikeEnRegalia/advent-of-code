package aoc2016

fun main() = readln().toInt().let {
    part1(it)
    part2(it)
}

private fun part1(count: Int) {
    val elves = ArrayDeque<Elf>().apply { repeat(count) { add(Elf(it + 1)) } }
    while (elves.size > 1) {
        val elf = elves.removeFirst()
        val stealFrom = elves.removeFirst()
        elves.add(Elf(elf.n, elf.presents + stealFrom.presents))
    }
    println(elves.first().n)
}

private fun part2(count: Int) {
    val left = ArrayDeque<Elf>()
    val right = ArrayDeque<Elf>()
    repeat(count) { n ->
        val elf = Elf(n + 1)
        if (n <= count / 2) left.add(elf) else right.add(elf)
    }
    var elf = left.removeFirst()

    while (left.isNotEmpty()) {
        elf = elf.copy(presents = elf.presents + left.removeLast().presents)
        right.add(elf)
        while (right.size >= left.size) left.add(right.removeFirst())
        elf = left.removeFirst()
    }
    println(elf.n)
}

private data class Elf(val n: Int, val presents: Int = 1)
