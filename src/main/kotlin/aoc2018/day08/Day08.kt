package aoc2018.day08

fun main() {
    with(readln().split(" ").map { it.toInt() }) {
        sumNodes { metadata, children -> children.sum() + metadata.sum() }.value.also { println(it) }
        sumNodes { metadata, children ->
            if (children.isEmpty()) metadata.sum() else {
                metadata.filter { it in 1..children.size }.sumOf { children[it - 1] }
            }
        }.value.also { println(it) }
    }
}

fun List<Int>.sumNodes(pos: Int = 0, value: (List<Int>, List<Int>) -> Int): IndexedValue<Int> =
    (1..this[pos])
        .fold(listOf(IndexedValue(pos + 2, 0))) { nodes, _ -> nodes.plus(sumNodes(nodes.last().index, value)) }
        .let { nodes ->
            countTo(this[pos + 1] - 1)
                .map { this[nodes.last().index + it] }
                .let { metadata ->
                    IndexedValue(nodes.last().index + metadata.size, value(metadata, nodes.drop(1).map { it.value }))
                }
        }

fun countTo(to: Int) = 0..to