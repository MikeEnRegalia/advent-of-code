package y2018.day08

fun main() {
    with(readLine()!!.split(" ").map { it.toInt() }) {
        sumNodes { metadata, children -> children.sum() + metadata.sum() }.value.also { println(it) }
        sumNodes { metadata, children ->
            if (children.isEmpty()) metadata.sum() else {
                metadata.filter { it in 1..children.size }.sumOf { children[it - 1] }
            }
        }.value.also { println(it) }
    }
}

fun List<Int>.sumNodes(pos: Int = 0, value: (List<Int>, List<Int>) -> Int): IndexedValue<Int> =
    mutableListOf(IndexedValue(pos + 2, 0))
        .also { nodes -> this[pos].times { nodes.add(sumNodes(nodes.last().index, value)) } }
        .let { nodes ->
            (0 until this[pos + 1])
                .map { this[nodes.last().index + it] }
                .let { metadata ->
                    IndexedValue(nodes.last().index + metadata.size, value(metadata, nodes.drop(1).map { it.value }))
                }
        }

inline fun Int.times(f: (Int) -> Unit) = repeat(this, f)