package aoc2023

fun main() {

    fun String.hash() = fold(0) { acc, c -> (17 * (acc + c.code)) % 256 }

    val input = generateSequence(::readLine)
        .single()
        .split(",")

    val part1 = input.sumOf(String::hash)

    println(part1)

    data class Lens(val label: String, val focalLength: Int) {
        override fun toString() = "[$label, $focalLength]"
    }

    val boxes = Array(256) { ArrayDeque<Lens>() }

    input.forEach { s ->
        when {
            "=" in s -> {
                val (label, focalLength) = s.split("=")
                val lens = Lens(label, focalLength.toInt())
                val boxIndex = label.hash()
                val box = boxes[boxIndex]
                val index = box.indexOfFirst { it.label == label }
                if (index == -1) box.add(lens)
                else box[index] = lens
            }

            else -> {
                val label = s.split("-").first()
                val boxIndex = label.hash()
                val box = boxes[boxIndex]
                box.removeIf { it.label == label }
            }
        }
    }

    val part2 = boxes.foldIndexed(0) { boxIndex, acc, box ->
        var boxResult = 0
        for ((slot, lens) in box.withIndex()) {
            boxResult += (boxIndex + 1) * (slot + 1) * lens.focalLength
        }
        acc + boxResult
    }

    println(part2)
}