package y2018.day08

fun main() {
    val input = readLine()!!.split(" ").map { it.toInt() }

    var sum = 0
    input.readNodes() { sum += it }
    println(sum)

    println(input.sumNodes().value)
}

fun List<Int>.readNodes(pos: Int = 0, onMetaData: (Int) -> Unit): Int {
    val children = this[pos]
    val metadata = this[pos + 1]
    var newPos = pos + 2
    for (i in 1..children) newPos = readNodes(newPos, onMetaData)
    for (i in 1..metadata) onMetaData(this[newPos++])
    return newPos
}

fun List<Int>.sumNodes(pos: Int = 0): IndexedValue<Int> {
    val childrenCount = this[pos]
    val metadata = this[pos + 1]
    if (childrenCount == 0) return IndexedValue(pos + 2 + metadata, subList(pos + 2, pos + 2 + metadata).sum())

    val children = mutableListOf<IndexedValue<Int>>()
    repeat(childrenCount) {
        val newPos = if (children.isEmpty()) pos + 2 else children.last().index
        children.add(sumNodes(newPos))
    }

    var sum = 0
    for (i in 0 until metadata) {
        val childIndex = this[children.last().index + i]
        if (childIndex in 1..children.size) sum += children[childIndex - 1].value
    }

    return IndexedValue(children.last().index + metadata, sum)
}