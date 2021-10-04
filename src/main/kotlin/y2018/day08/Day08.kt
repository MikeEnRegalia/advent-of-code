package y2018.day08

fun main() {
    val input = readLine()!!.split(" ").map { it.toInt() }

    var sum = 0
    input.readNodes() { sum += it }
    println(sum)
}

fun List<Int>.readNodes(pos: Int = 0, onMetaData: (Int) -> Unit): Int {
    val children = this[pos]
    val metadata = this[pos + 1]
    var newPos = pos + 2
    for (i in 1..children) newPos = readNodes(newPos, onMetaData)
    for (i in 1..metadata) onMetaData(this[newPos++])
    return newPos
}