package y2018.day08

fun main() {
    with(readLine()!!.split(" ").map { it.toInt() }) {
        println(sumNodes().value)
        println(sumNodes(simple = false).value)
    }
}

fun List<Int>.sumNodes(pos: Int = 0, simple: Boolean = true): IndexedValue<Int> {
    val childrenCount = this[pos]

    val nodeAndChildren = mutableListOf(IndexedValue(pos + 2, 0))
    repeat(childrenCount) { with(nodeAndChildren) { add(sumNodes(last().index, simple)) } }

    val metadataCount = this[pos + 1]
    val metadata = (0 until metadataCount)
        .map { this[nodeAndChildren.last().index + it] }

    val sum = when (simple) {
        true -> metadata.sum() + nodeAndChildren.sumOf { it.value }
        false -> when (childrenCount) {
            0 -> metadata.sum()
            else -> metadata
                .filter { it in 1 until nodeAndChildren.size }
                .sumOf { nodeAndChildren[it].value }
        }
    }

    return IndexedValue(nodeAndChildren.last().index + metadataCount, sum)
}