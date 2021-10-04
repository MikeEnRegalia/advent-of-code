package y2018.day08

fun main() = with(readLine()!!.split(" ").map { it.toInt() }) {
    println(sumNodes().value)
    println(sumNodes(simple = false).value)
}

fun List<Int>.sumNodes(pos: Int = 0, simple: Boolean = true): IndexedValue<Int> =
    mutableListOf(IndexedValue(pos + 2, 0)).let { nodes ->
        repeat(this[pos]) { with(nodes) { add(sumNodes(last().index, simple)) } }

        val metadata = (0 until this[pos + 1]).map { this[nodes.last().index + it] }
        with(metadata) {
            if (simple) nodes.sumOf { it.value } + sum()
            else if (nodes.size == 1) sum()
            else filter { it in 1 until nodes.size }
                .sumOf { nodes[it].value }
        }.let { IndexedValue(nodes.last().index + metadata.size, it) }
    }