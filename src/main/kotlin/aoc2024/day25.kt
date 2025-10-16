package aoc2024

fun main() {
    val SIZE = 5

    val (locks, keys) = generateSequence(::readLine).filter(String::isNotBlank)
        .chunked(SIZE + 2)
        .partition { "#" in it.first() }.let { keysAndLocks ->
            keysAndLocks.toList().map { data ->
                data.map { lines ->
                    lines.drop(1).dropLast(1).run {
                        (0..<SIZE).map { i -> count { it[i] == '#' } }
                    }
                }
            }
        }

    println(locks.sumOf { lock -> keys.count { key -> lock.zip(key).none { (a, b) -> a + b > SIZE } } })
}
