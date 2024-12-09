package aoc2024

fun main() {
    data class Chunk(val id: Int?, val size: Int)

    val originalChunks = buildMap {
        var index = 0
        readln().map(Char::digitToInt).forEachIndexed { i, size ->
            this[index] = Chunk(if (i % 2 == 0) i / 2 else null, size)
            index += size
        }
    }

    fun buildList(chunks: Map<Int, Chunk>) = mutableListOf<Int>().apply {
        chunks.entries.toList().sortedBy { it.key }.forEach { (pos, chunk) ->
            repeat(chunk.size) {
                add(chunk.id ?: -1)
            }
        }
    }


    val part1 = buildList(originalChunks).also { fs ->
        var nextFree = -1
        var nextFile = fs.size
        while (true) {
            while (nextFree < 0 || fs[nextFree] != -1) nextFree++
            while (nextFile >= fs.size || fs[nextFile] == -1) nextFile--
            if (nextFree > nextFile) break
            fs[nextFree] = fs[nextFile]
            fs[nextFile] = -1
        }
    }

    val part2 = originalChunks.toMutableMap().let { new ->
        val fileIDs = new.entries.mapNotNull { it.value.id }
        for (fileId in fileIDs.sorted().reversed()) {
            val (fileIndex, fileChunk) = new.entries.single { it.value.id == fileId }
            val (freeIndex, freeChunk) = new.entries
                .filter { it.value.id == null && it.key < fileIndex && it.value.size >= fileChunk.size }
                .minByOrNull { it.key } ?: continue

            new[fileIndex] = Chunk(null, fileChunk.size)
            new[freeIndex] = fileChunk

            val newFreeSize = freeChunk.size - fileChunk.size
            if (newFreeSize > 0) {
                val newFreeIndex = freeIndex + fileChunk.size
                new[newFreeIndex] = Chunk(null, newFreeSize)
            }
        }
        buildList(new)
    }


    listOf(part1, part2).map { fs ->
        fs.mapIndexedNotNull { i, it -> if (it == -1) null else (i * it).toLong() }.sum()
    }.also(::println)
}
