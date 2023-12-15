package aoc2023

fun main() {
    val input = generateSequence(::readLine).single().split(",")

    fun String.hash() = fold(0) { acc, c -> (17 * (acc + c.code)) % 256 }
    println(input.sumOf(String::hash))

    data class Lens(val label: String, val focalLength: Int)

    val boxes = Array(256) { mutableListOf<Lens>() }
    input.forEach { s ->
        when {
            "=" in s -> {
                val lens = s.split("=").let { Lens(it[0], it[1].toInt()) }
                val box = boxes[lens.label.hash()]
                when (val index = box.indexOfFirst { it.label == lens.label }) {
                    -1 -> box.add(lens)
                    else -> box[index] = lens
                }
            }

            else -> {
                val label = s.split("-").first()
                boxes[label.hash()].removeIf { it.label == label }
            }
        }
    }

    boxes.foldIndexed(0) { boxIndex, acc, box ->
        acc + box.foldIndexed(0) { slot, boxAcc, lens -> boxAcc + (boxIndex + 1) * (slot + 1) * lens.focalLength }
    }.also(::println)
}