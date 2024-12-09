package aoc2024

fun main() {
    data class Chunk(val size: Int, val id: Int? = null)

    val chunks = readln().map(Char::digitToInt).foldIndexed(mutableMapOf<Int, Chunk>()) { i, acc, size ->
        acc.apply { this[values.sumOf { it.size }] = Chunk(size, if (i % 2 == 0) i / 2 else null) }
    }

    fun Map<Int, Chunk>.toFS() = entries.sortedBy { it.key }.map { it.value }
        .flatMap { (size, id) -> (0..<size).map { id } }

    fun part1() = chunks.toFS().toMutableList().also { fs ->
        var free = -1
        var file = fs.size
        while (true) {
            while (free < 0 || fs[free] != null) free++
            while (file >= fs.size || fs[file] == null) file--
            if (free > file) break
            fs[free] = fs[file]
            fs[file] = null
        }
    }

    fun part2() = chunks.toMutableMap().apply {
        for (id in mapNotNull { it.value.id }.sortedByDescending { it }) {
            val (fileIndex, fileChunk) = entries.single { it.value.id == id }
            val (freeIndex, freeChunk) = entries
                .filter { it.value.id == null && it.key < fileIndex && it.value.size >= fileChunk.size }
                .minByOrNull { it.key } ?: continue

            this[fileIndex] = Chunk(fileChunk.size)
            this[freeIndex] = fileChunk

            val remainingFree = freeChunk.size - fileChunk.size
            if (remainingFree > 0)
                this[freeIndex + fileChunk.size] = Chunk(remainingFree)
        }
    }.toFS()

    fun List<Int?>.checksum() = mapIndexedNotNull { i, it -> it?.times(i)?.toLong() }.sum()

    sequenceOf(::part1, ::part2).map { it().checksum() }.forEach(::println)
}
