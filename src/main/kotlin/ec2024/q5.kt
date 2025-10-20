package ec2024

fun main() {
    val columns = generateSequence(::readLine)
        .map { row -> row.split(" ").map { it.toInt() } }
        .toList()
        .let { grid -> grid.first().indices.map { col -> grid.map { row -> row[col] }.toMutableList() } }
        .toMutableList()

    val counts = mutableMapOf<String, Int>()

    var columnIndex = 0
    var round = 1

    var check2024 = true

    fun state() = "$columnIndex $columns"
    val states = mutableSetOf(state())

    while(true) {
        val clapping = columns[columnIndex].removeFirst()
        columnIndex = (columnIndex + 1) % columns.size

        var up = false
        var pos = -1
        repeat(clapping) {
            when {
                up -> {
                    pos--
                    if (pos < 0) {
                        pos++
                        up = false
                    }
                }
                else -> {
                    pos++
                    if (pos > columns[columnIndex].lastIndex) {
                        pos--
                        up = true
                    }
                }
            }
        }

        columns[columnIndex].add(if (up) pos + 1 else pos, clapping)

        val shout = columns.map { it.first() }.joinToString("")

        val count = counts.compute(shout) { _, v -> (v ?: 0) + 1 }
        if (round == 10) {
            println("round 10: $shout")
        }

        if (check2024 && count == 2024) {
            println("2024: ${shout.toLong() * round}")
            check2024 = false
        }
        round++

        if (!states.add(state())) {
            println("max: ${counts.keys.max()}")
            break
        }
    }
}
