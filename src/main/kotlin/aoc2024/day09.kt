package aoc2024

fun main() {
    var file = -1
    val data = buildList {
        readln().map(Char::digitToInt).chunked(2).forEach {
            file++
            repeat(it[0]) { add(file) }
            if (it.size == 2) {
                repeat(it[1]) { add(-1) }
            }
        }
    }

    val part1 = data.toMutableList().also { fs ->
        var nextFree = -1
        var nextFile = data.size
        while (true) {
            while (nextFree < 0 || fs[nextFree] != -1) nextFree++
            while (nextFile >= data.size || fs[nextFile] == -1) nextFile--
            if (nextFree > nextFile) break
            fs[nextFree] = data[nextFile]
            fs[nextFile] = -1
        }
    }
    println(part1.mapIndexedNotNull { i, it -> if (it == -1) null else (i * it).toLong() }.sum())
}
