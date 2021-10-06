package y2018.day08

fun main() = with(readLine()!!.split(" ").map { it.toInt() }) {
    println(sumNodes().value)
    println(sumNodes(simple = false).value)
}

fun List<Int>.sumNodes(pos: Int = 0, simple: Boolean = true): IndexedValue<Int> =
    mutableListOf(IndexedValue(pos + 2, 0))
        .also { nodes -> this[pos].times { nodes.add(sumNodes(nodes.last().index, simple)) } }
        .let { nodes ->
            (0 until this[pos + 1])
                .map { this[nodes.last().index + it] }
                .let { metadata ->
                    IndexedValue(nodes.last().index + metadata.size, metadata.value(simple, nodes.drop(1)))
                }
        }

private fun List<Int>.value(simple: Boolean, nodes: List<IndexedValue<Int>>) = when {
    simple -> nodes.sumOf { it.value } + sum()
    nodes.isEmpty() -> sum()
    else -> filter { it in 1..nodes.size }.sumOf { nodes[it - 1].value }
}

inline fun Int.times(f: (Int) -> Unit) = repeat(this, f)