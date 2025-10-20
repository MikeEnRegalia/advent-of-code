package ec2024

fun main() {
    val values = generateSequence(::readLine).map { it.toInt()} .toList()
    val min = values.min()
    println(values.sumOf { it - min })
}
