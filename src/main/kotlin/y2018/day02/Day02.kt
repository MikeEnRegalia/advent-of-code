package y2018.day02

fun main() {
    val boxes = generateSequence(::readLine).toList()
    val boxesLetterCounts = boxes.map { box -> box.groupingBy { it }.eachCount() }

    val count1 = boxesLetterCounts.count { it.values.any { it == 2 } }
    val count2 = boxesLetterCounts.count { it.values.any { it == 3 } }
    (count1 * count2).also { println(it) }

    (1 until boxes.minOf { it.length })
        .map { i -> boxes.map { it.substring(0, i) + it.substring(i+1) }}
        .map { it.groupingBy { it }.eachCount() }
        .map { it.filter { it.value == 2 }.keys }
        .filter { it.isNotEmpty() }
        .also { println(it) }
}
