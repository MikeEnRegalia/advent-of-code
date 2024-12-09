package aoc2024

fun main() {
    data class Chunk(val id: Int?, val size: Int)

    val originalChunks = buildMap<Int, Chunk> {
        for ((i, size) in readln().map(Char::digitToInt).withIndex()) {
            this[values.sumOf { it.size }] = Chunk(if (i % 2 == 0) i / 2 else null, size)
        }
    }

    fun Map<Int, Chunk>.buildList() = mutableListOf<Int?>().also { list ->
        entries.sortedBy { it.key }.map { it.value }
            .forEach { (id, size) -> repeat(size) { list.add(id) } }
    }

    fun part1() = originalChunks.buildList().also { fs ->
        var nextFree = -1
        var nextFile = fs.size
        while (true) {
            while (nextFree < 0 || fs[nextFree] != null) nextFree++
            while (nextFile >= fs.size || fs[nextFile] == null) nextFile--
            if (nextFree > nextFile) break
            fs[nextFree] = fs[nextFile]
            fs[nextFile] = null
        }
    }

    fun part2() = originalChunks.toMutableMap().also { new ->
        for (id in new.mapNotNull { it.value.id }.sortedByDescending { it }) {
            val (fileIndex, fileChunk) = new.entries.single { it.value.id == id }
            val (freeIndex, freeChunk) = new.entries
                .filter { it.value.id == null && it.key < fileIndex && it.value.size >= fileChunk.size }
                .minByOrNull { it.key } ?: continue

            new[fileIndex] = Chunk(null, fileChunk.size)
            new[freeIndex] = fileChunk

            val newFreeSize = freeChunk.size - fileChunk.size
            if (newFreeSize > 0) {
                new[freeIndex + fileChunk.size] = Chunk(null, newFreeSize)
            }
        }
    }.buildList()

    fun List<Int?>.checksum() = mapIndexedNotNull { i, it -> it?.times(i)?.toLong() }.sum()

    sequenceOf(::part1, ::part2).map { it().checksum() }.forEach(::println)
}
