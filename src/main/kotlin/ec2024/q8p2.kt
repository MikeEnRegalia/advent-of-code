package ec2024

fun main() {
    val priests = readln().toInt()

    var thickness = 1
    var width = 1
    var totalBlocks = 1

    val acolytes = 1111

    val material = 20240000

    while (true) {
        width += 2
        thickness = (thickness * priests) % acolytes
        val blocks = thickness * width
        if (material in totalBlocks..<totalBlocks + blocks) {
            println(width * (totalBlocks + blocks - material))
            break
        }
        totalBlocks += blocks
    }
}
