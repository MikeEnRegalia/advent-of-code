package aoc2021b

fun main() {
    fun String.parseBits() = map { it.toString().toInt(16) }.joinToString("") { it.toString(2).padStart(4, '0') }
    println(Parser(readln().parseBits()).evaluate())
}

data class Packet(val version: Int, val value: Long)

class Parser(val data: String, private var pos: Int = 0) {
    fun evaluate() = readPacket()

    private fun readPacket(): Packet {
        val version = read(3).toInt(2)
        return when (val type = read(3).toInt(2)) {
            4 -> readLiteral(version)
            else -> readOperator(version, type)
        }
    }

    private fun readLiteral(version: Int) = Packet(version, buildString {
        while (true) {
            val wasLast = read(1) == "0"
            append(read(4))
            if (wasLast) break
        }
    }.toLong(2))

    private fun readOperator(version: Int, type: Int) = with(readOperatorRaw()) {
        Packet(version + sumOf { it.version }, map { it.value }.calc(type))
    }

    private fun readOperatorRaw() = if (read(1) == "0") readOperator0() else readOperator1()

    private fun readOperator0() = buildList {
        val length = read(15).toInt(2)
        val start = pos
        while (pos < start + length) add(readPacket())
    }

    private fun readOperator1() = buildList {
        repeat(read(11).toInt(2)) { add(readPacket()) }
    }

    private fun List<Long>.calc(type: Int) = when (type) {
        0 -> sum()
        1 -> reduce(Long::times)
        2 -> minOf { it }
        3 -> maxOf { it }
        5 -> if (get(0) > get(1)) 1 else 0
        6 -> if (get(0) < get(1)) 1 else 0
        7 -> if (get(0) == get(1)) 1 else 0
        else -> throw IllegalArgumentException(type.toString())
    }

    private fun read(amount: Int) = data.substring(pos until pos + amount).also { pos += amount }
}