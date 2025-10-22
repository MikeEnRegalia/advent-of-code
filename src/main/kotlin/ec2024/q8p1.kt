package ec2024

fun main() {
    val availableBlocks = readln().toInt()

    var layerBlocks = 1
    var totalWidth = 1
    var totalBlocks = 1

    while (true) {
        layerBlocks += 2
        totalWidth += 2

        val next = totalBlocks + layerBlocks
        if (availableBlocks in totalBlocks..<next) {
            println((next - availableBlocks) * totalWidth)
            break
        }
        totalBlocks += layerBlocks
    }
}
